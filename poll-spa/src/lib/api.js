/// <reference types="vite/client" />

const BASE = (import.meta.env.VITE_API_BASE_URL ?? '').replace(/\/$/, '');

/**
 * Minimal JSON fetch helper.
 * @param {string} path
 * @param {{ method?: string, json?: any }} [opts]
 */
async function http(path, opts = {}) {
  const { method = 'GET', json } = opts;

  const res = await fetch(`${BASE}${path}`, {
    method,
    headers: json ? { 'Content-Type': 'application/json' } : undefined,
    body: json ? JSON.stringify(json) : undefined,
  });

  if (!res.ok) {
    const text = await res.text().catch(() => '');
    throw new Error(`${res.status} ${res.statusText} — ${text}`);
  }

  const ct = res.headers.get('content-type') || '';
  return ct.includes('application/json') ? res.json() : {};
}


/* ------------------- Users ------------------- */
export async function createUser(username) {
  return http('/users', { method: 'POST', json: { username } });
}

/* ------------------- Polls ------------------- */
export async function createPoll({ question, options, username }) {
    // 1) Create the poll (just the question/title)
    const created = await http(`/polls?username=${encodeURIComponent(username)}`, {
      method: 'POST',
      json: { question } 
    });
  
    const pid = created.id;
    for (const t of options.map(s => s.trim()).filter(Boolean)) {
      let lastErr;
      for (const attempt of [
        { path: `/polls/${pid}/options`, json: { text: t } },
        { path: `/polls/${pid}/options`, json: { name: t } },
        { path: `/polls/${pid}/options?text=${encodeURIComponent(t)}` },
      ]) {
        try {
          if (attempt.json) { await http(attempt.path, { method: 'POST', json: attempt.json }); }
          else { await http(attempt.path, { method: 'POST' }); }
          lastErr = null; break;
        } catch (e) { lastErr = e; }
      }
      if (lastErr) throw lastErr;
    }
    return getPoll(pid);}
  
export async function getPoll(pollId) {
  return http(`/polls/${encodeURIComponent(pollId)}`);
}

export async function vote({ pollId, optionId, username }) {
    if (optionId == null) {
      throw new Error('This option has no id from the API, so it cannot be voted on.');
    }
  
    const base = `/polls/${encodeURIComponent(pollId)}/votes`;
    const u = encodeURIComponent(username);
    const id = encodeURIComponent(optionId);
  
    try {
      return await http(`${base}?username=${u}&optionId=${id}`, { method: 'POST' });
    } catch (e1) {
      try {
        return await http(`${base}?username=${u}&voteOptionId=${id}`, { method: 'POST' });
      } catch (e2) {
        try {
          return await http(`${base}?username=${u}&option=${id}`, { method: 'POST' });
        } catch {
          throw e1;
        }
      }
    }
  }

/* ------------ Optional: UI normalization helper ------------ */
export function normalizePoll(p) {
    const rawOptions = (p.options ?? p.voteOptions ?? p.choices ?? p.answers ?? []);
    const options = rawOptions.map((o, i) => {
      if (typeof o === 'string') {
        return { id: i + 1, label: o, count: 0 };
      }
      
      const id =
        o.id ?? o.optionId ?? o.voteOptionId ?? o.choiceId ?? o.key ?? o.value ?? (i + 1);

      const labelKeys = [
        'text', 'title', 'name', 'option', 'optionText', 'label', 'value', 'description', 'content'
      ];

      let label = labelKeys.map(k => o[k]).find(v => v != null && `${v}`.trim() !== '');
      if (label == null) label = `Option ${i + 1}`;

      const count =
        o.votes ?? o.voteCount ?? o.count ?? (Array.isArray(o.votesList) ? o.votesList.length : 0) ?? 0;
  
      return { id, label: String(label), count: Number(count) || 0 };
    });
  
    return {
      id: p.id ?? p.pollId,
      question: p.question ?? p.title ?? p.text ?? '',
      options,
    };
  }
