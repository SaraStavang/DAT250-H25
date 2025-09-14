import { writable } from 'svelte/store';

function persistentWritable(key, initial) {
  const stored =
    typeof localStorage !== 'undefined' ? localStorage.getItem(key) : null;
  const start = stored ? JSON.parse(stored) : initial;

  const store = writable(start);
  store.subscribe((v) => {
    try {
      localStorage.setItem(key, JSON.stringify(v));
    } catch {}
  });
  return store;
}

export const currentUser = persistentWritable('currentUser', null);
export const lastCreatedPoll = persistentWritable('lastCreatedPoll', null);
