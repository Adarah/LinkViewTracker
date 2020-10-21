ALTER TABLE visitor
DROP COLUMN id;

ALTER TABLE visitor
ADD PRIMARY KEY (ip, redirect_alias);