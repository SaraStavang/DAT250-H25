<script>
    import { createPoll } from '../lib/api.js';
    import { currentUser, lastCreatedPoll } from '../lib/stores.js';
  
    let question = '';
    let options  = ['', ''];
    let loading  = false;
    let error    = '';
    let success  = '';
  
    const addOption = () => (options = [...options, '']);
    const removeOption = (i) => (options = options.filter((_, idx) => idx !== i));
  
    async function onSubmit(e) {
      e.preventDefault();
      error = success = '';
  
      const clean = options.map(o => o.trim()).filter(Boolean);
  
      if (!question.trim())       { error = 'Question is required.'; return; }
      if (clean.length < 2)       { error = 'Add at least two options.'; return; }
      if (!$currentUser?.username){ error = 'Create a user first on the Create User tab.'; return; }
  
      loading = true;
      try {
        const poll = await createPoll({
          question: question.trim(),
          options: clean,
          username: $currentUser.username
        });
  
        $lastCreatedPoll = poll;
        success = `Poll created (id: ${poll.id})`;
        question = '';
        options = ['', ''];
      } catch (e) {
        error = e?.message || 'Request failed';
      } finally {
        loading = false;
      }
    }
  </script>
  
  <div class="card">
    <h1>Create Poll</h1>
  
    <form on:submit|preventDefault={onSubmit}>
      <label for="q">Question</label>
      <input id="q" type="text" bind:value={question} placeholder='e.g., "Pineapple on pizza?"' />
  
      <div style="margin-top:.75rem">
        <label>Options</label>
  
        {#each options as opt, i}
          <div class="row" style="margin-bottom:.5rem">
            <input type="text" bind:value={options[i]} placeholder={`Option ${i+1}`} />
            <button type="button" class="ghost" on:click={() => removeOption(i)} disabled={options.length <= 2}>
              Remove
            </button>
          </div>
        {/each}
  
        <button type="button" class="secondary" on:click={addOption}>+ Add option</button>
      </div>
  
      <div style="margin-top:.75rem">
        <button disabled={loading}>{loading ? 'Creating…' : 'Create Poll'}</button>
      </div>
  
      {#if $lastCreatedPoll}
        <p class="help" style="margin-top:.5rem">Last poll id: <b>{$lastCreatedPoll.id}</b></p>
      {/if}
      {#if success}<p class="success" style="margin-top:.5rem">{success}</p>{/if}
      {#if error}<p class="error" style="margin-top:.5rem">{error}</p>{/if}
    </form>
  </div>
  