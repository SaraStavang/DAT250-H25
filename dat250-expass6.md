I didn’t manage to get the voting feature fully working in time (I think I made the setup a bit to complicated since I wanted it to look nice). While the votes are sent and accepted by the backend, the vote counts aren’t updating in the frontend. I believe the issue is that the backend doesn't return the number of votes per option when fetching the poll data. The frontend expects this information to display the updated vote counts. I think the solution would be to modify the backend to return a response that includes the vote totals for each option, so the frontend can show the correct results after voting.

Link to the task:
[Poll SPA frontend](./poll-spa/)
