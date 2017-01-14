#!/usr/bin/env bash

TABLES=(category level permission program_language role role_permission variable)
AI_TABLES=(contest contest_solution notice problem solution user)
PARAMS=""
for TABLE in "${TABLES[@]}"; do
  PARAMS="${PARAMS} --ignore-table=${TABLE}"
done

read -s -p "mysql password: " PASSWORD
echo

mysqldump -u root --password=${PASSWORD} -B --no-data --single-transaction oj > conf/oj.sql

sed -i 's/ AUTO_INCREMENT=[0-9]*\b//' conf/oj.sql
for TABLE in "${AI_TABLES[@]}"; do
  echo >> conf/oj.sql
  echo "ALTER TABLE ${TABLE} AUTO_INCREMENT=1000;" >> conf/oj.sql
done

mysqldump -u root --password=${PASSWORD} --no-create-info --extended-insert=FALSE --single-transaction --where="uid=1000" oj user user_ext user_role >> conf/oj.sql
mysqldump -u root --password=${PASSWORD} --no-create-info --extended-insert=FALSE --single-transaction oj "${TABLES[@]}" >> conf/oj.sql
sed -i "s/'emailUser','[^']*'/'emailUser',''/g;s/'emailPass','[^']*'/'emailPass',''/g" conf/oj.sql

#mysqldump -u root --password=${PASSWORD} oj | sed -e "s/\\\'/''/g" > oj-data.sql
