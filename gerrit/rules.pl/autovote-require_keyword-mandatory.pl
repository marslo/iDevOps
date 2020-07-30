submit_rule(S) :-
  gerrit:default_submit(X), % get the current submit structure
  X=.. [submit | Ls],
  require_ticket_check_for_ticket(Ls, Nls),
  S=.. [submit | Nls].

require_ticket_check_for_ticket(S1, S2) :-
   gerrit:commit_message_matches('\\[issue-[\\d]{2}\\][\\s\\S]+'),
   !,
   S2 = [label('Ticket-Checked', ok(user(790))) | S1]. % Add the label and automatically approval by user-id: 790

require_ticket_check_for_ticket(S1, [label('Ticket-Checked', need(_)) | S1]).
