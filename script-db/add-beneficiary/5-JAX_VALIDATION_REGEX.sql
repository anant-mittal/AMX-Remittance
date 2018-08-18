insert into JAX_VALIDATION_REGEX (KEY, VALUE, DESCRIPTION)
values ('ARABIC_NAME', '^[\u0621-\u064A\u0660-\u0669 ]+$', 'Arabic person name');

insert into JAX_VALIDATION_REGEX (KEY, VALUE, DESCRIPTION)
values ('ALPHANUMERIC', '[A-Za-z0-9]', 'test');

insert into JAX_VALIDATION_REGEX (KEY, VALUE, DESCRIPTION)
values ('ALPHABETS_1', '[a-zA-Z ,.''-]+', 'Only alphabets with ,.''- chars also space');

Commit;
/

