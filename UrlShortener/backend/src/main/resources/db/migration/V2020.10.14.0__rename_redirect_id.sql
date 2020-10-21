ALTER TABLE redirect
RENAME id TO alias;

ALTER TABLE visitor
RENAME redirect_id TO redirect_alias;

ALTER TABLE visitor
RENAME CONSTRAINT visitor_redirect_id_fkey TO visitor_redirect_alias_fkey;
