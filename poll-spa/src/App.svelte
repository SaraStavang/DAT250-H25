<script>
  import { onMount } from 'svelte';
  import CreateUser from './components/CreateUser.svelte';
  import CreatePoll from './components/CreatePoll.svelte';
  import Vote from './components/Vote.svelte';
  import { currentUser } from './lib/stores';

  let route = window.location.hash || '#/create-user';

  const routes = {
    '#/create-user': CreateUser,
    '#/create-poll': CreatePoll,
    '#/vote': Vote,
  };

  function updateRoute() {
    route = window.location.hash || '#/create-user';
  }

  onMount(() => {
    window.addEventListener('hashchange', updateRoute);
    return () => window.removeEventListener('hashchange', updateRoute);
  });

  $: Active = routes[route] || CreateUser;
</script>

<nav>
  <a href="#/create-user" class:active={route === '#/create-user'}>Create User</a>
  <a href="#/create-poll" class:active={route === '#/create-poll'}>Create Poll</a>
  <a href="#/vote" class:active={route === '#/vote'}>Vote</a>
  <span style="margin-left:auto" class="help">
    {$currentUser ? `Signed in as: ${$currentUser.name} (id: ${$currentUser.id})` : 'No user yet'}
  </span>
</nav>

<div class="container">
  <svelte:component this={Active} />
</div>
