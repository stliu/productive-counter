/top_coders

{
	month: 4,
	committers: [
		{
			lines:  20,
			name: stliu
		}

	]

}

create table monthly_commit(
	month int,
	username text,

);


CREATE TABLE monthly_commit(
  lines counter,
  year int,
  month int,
  username text,
  PRIMARY KEY (month, username)
);


UPDATE monthly_commit SET lines = lines + 1 WHERE month=5 AND username='stliu';
UPDATE monthly_commit SET lines = lines + 2 WHERE month=4 AND username='stliu';
UPDATE monthly_commit SET lines = lines + 3 WHERE month=3 AND username='stliu';
UPDATE monthly_commit SET lines = lines + 4 WHERE month=2 AND username='stliu';
UPDATE monthly_commit SET lines = lines + 5 WHERE month=1 AND username='stliu';