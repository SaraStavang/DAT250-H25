<script>
    import { createUser } from '../lib/api.js';
    import { currentUser } from '../lib/stores.js';
  
    let name = '';
    let loading = false;
    let msg = '';
    let err = '';
  
    async function onSubmit(e) {
      e.preventDefault();
      msg = err = '';
      if (!name.trim()) { err = 'Please enter a name.'; return; }
      loading = true;
      try {
        const user = await createUser(name.trim());
        $currentUser = user;              
        msg = `User created: ${user.name} (id: ${user.id})`;
        name = '';
      } catch (e) {
        err = e.message;
      } finally {
        loading = false;
      }
    }
  </script>
  
  <div class="card">
    <h1>Create User</h1>
    <form on:submit|preventDefault={onSubmit}>
      <label for="name">Name</label>
      <input id="name" type="text" bind:value={name} placeholder="e.g., Alice" />
      <div style="margin-top:.75rem">
        <button disabled={loading}>{loading ? 'Creating…' : 'Create User'}</button>
        <button type="button" class="secondary" on:click={() => { name=''; msg=''; err=''; }}>Reset</button>
      </div>
      {#if msg}<p class="success" style="margin-top:.75rem">{msg}</p>{/if}
      {#if err}<p class="error" style="margin-top:.75rem">{err}</p>{/if}
    </form>
  </div>
  