<script>
    import { getPoll, vote, normalizePoll } from '../lib/api.js';
    import { currentUser } from '../lib/stores.js';
  
    let pollId = '';
    let poll = null;       
    let raw = null;        
    let loading = false;
    let err = '';
    let msg = '';
  
  
    async function loadPoll() {
      err = msg = '';
      poll = null;
      if (!pollId.trim()) { err = 'Enter a poll id'; return; }
      loading = true;
      try {
        raw = await getPoll(pollId.trim());
        poll = normalizePoll(raw);
        console.log('RAW POLL:', raw);
        console.log('UI POLL:', poll);
        if (!poll.options?.length) {
          msg = 'No options found on this poll.';
        }
      } catch (e) {
        err = e.message || 'Failed to load poll';
      } finally {
        loading = false;
      }
    }
  
    async function cast(optionId) {
      err = msg = '';
      if (!$currentUser?.username) { err = 'Create a user first on the Create User tab.'; return; }
      loading = true;
      try {
        await vote({
          pollId: poll.id,
          optionId,
          username: $currentUser.username
        });

        raw = await getPoll(poll.id);
        console.log('RAW AFTER VOTE:', raw);
        poll = normalizePoll(raw);
        console.log('UPDATED POLL:', poll);
        msg = 'Vote recorded';
      } catch (e) {
        err = e.message || 'Vote failed';
      } finally {
        loading = false;
      }
    }
  </script>
  
  <div class="card">
    <h1>Vote on a Poll</h1>
  
    <div class="row">
      <input
        placeholder="Poll id, e.g. 1"
        bind:value={pollId}
        on:keydown={(e)=> e.key==='Enter' && loadPoll()}
      />
      <button class="secondary" on:click={loadPoll} disabled={loading}>Load Poll</button>
    </div>
  
    {#if err}<p class="error">{err}</p>{/if}
    {#if msg}<p class="ok">{msg}</p>{/if}
  
    {#if poll}
      <hr />
      <p class="muted">Poll#{poll.id} &nbsp; <strong>Question:</strong> {poll.question}</p>
  
      {#each poll.options as opt}
        <div class="optionRow">
          <div class="label">{opt.label}</div>
          <div class="actions">
            <button on:click={() => cast(opt.id)} disabled={loading}>Vote</button>
            <span class="count">{opt.count} votes</span>
          </div>
        </div>
      {/each}
    {/if}
  
    <p class="tip">Tip: after creating a poll, its id appears on the Create Poll page—paste it here and press “Load Poll”.</p>
  </div>
  
  <style>
    .card{background:#fff;padding:1.25rem;border-radius:1rem;box-shadow:0 6px 18px rgba(0,0,0,.08)}
    .row{display:flex;gap:.75rem;margin:.5rem 0 1rem}
    input{flex:1;padding:.9rem 1rem;border-radius:.8rem;border:1px solid #eee;background:#faf6f8}
    button{padding:.7rem 1rem;border-radius:.8rem;border:0;background:#4c7dff;color:#fff;font-weight:600}
    button.secondary{background:#e7e9ee;color:#2b2f38}
    .optionRow{display:flex;justify-content:space-between;align-items:center;padding:.6rem 0;border-bottom:1px dashed #f0dbe4}
    .label{font-size:1.05rem}
    .actions{display:flex;gap:.6rem;align-items:center}
    .count{color:#6b7280}
    .muted{color:#6b7280}
    .tip{color:#6b7280;margin-top:1rem}
    .error{color:#e11d48;font-weight:600}
    .ok{color:#16a34a;font-weight:600}
  </style>
  