submit_rule(S) :-
    gerrit:default_submit(X),
    X =.. [submit | Ls],
    require_ticket_check_for_ticket(Ls, R),
    S =.. [submit | R].

require_ticket_check_for_ticket(S1, S2) :-
    gerrit:commit_message_matches('\\[issue-[\\d]{2}\\]\\s?:\\s?[\\w\\W]+'),
    !,
    S2 = [label('Ticket-Checked', need(_)) | S1].

require_ticket_check_for_ticket(S1, S2) :-
    !, S2 = S1.
