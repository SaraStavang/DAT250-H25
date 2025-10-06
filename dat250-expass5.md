
Use Case 1: Keep track of logged-in users: Here is what I have done to keep track of logged-in users.

Used a Redis Set logged_in_users to track who’s online.
Result: final members = bob, eve; count = 2. This demonstrates membership checks and constant-time add/remove.

<img width="428" height="324" alt="Skjermbilde 2025-10-06 kl  13 17 06" src="https://github.com/user-attachments/assets/23d6aac6-e18e-4b1d-adf1-0baaf9283d88" />

Use Case 2: 

Verified with HGETALL showing fields like title, option:1:caption, option:1:votes, etc.
This supports atomic updates via HINCRBY (e.g., increment a single option’s votes without rewriting the object).

<img width="671" height="213" alt="Skjermbilde 2025-10-06 kl  13 25 10" src="https://github.com/user-attachments/assets/6f0a1636-f00b-4b49-8d00-c80230fdcc77" />

The cache implementation: 

The app log shows “DB HIT for poll 1” and the Hibernate query that groups by presentation_order, confirming the cache-aside flow is wired correctly (first request hits the DB, result is cacheable).

Current issue: the aggregation query groups by VoteOption.presentationOrder, but this field is not present/populated in the VoteOption entity. As a result, the query returns no rows and the cached response is empty. I am working on my code to fix this. 

<img width="1355" height="568" alt="Skjermbilde 2025-10-06 kl  19 38 57" src="https://github.com/user-attachments/assets/e7a506fb-a08d-4a53-b1e2-fb3e7ed69cbc" />

