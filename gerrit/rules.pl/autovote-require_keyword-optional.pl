submit_rule(S) :-
    gerrit:default_submit(X),
    X =.. [submit | Ls],
    require_ticket_check_for_ticket(Ls, R),
    S =.. [submit | R].

require_ticket_check_for_ticket(S1, S2) :-
    gerrit:commit_message_matches('^issues-[\\d]+\\s?:\\s?[\\w\\W]+'),
    !,
    S2 = [label('Ticket-Checked', ok(user(824))) | S1].

require_ticket_check_for_ticket(S1, S2) :-
    !, S2 = S1.
