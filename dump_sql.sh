#!/usr/bin/env bash

TABLES=(category level permission program_language role role_permission variable)
AI_TABLES=(contest contest_solution notice problem solution user)
PARAMS=""
for TABLE in "${TABLES[@]}"; do
  PARAMS="${PARAMS} --ignore-table=${TABLE}"
done

read -s -p "mysql password: " PASSWORD
echo

mysqldump -u root --password=${PASSWORD} --no-data --single-transaction oj > conf/oj.sql

sed -i 's/ AUTO_INCREMENT=[0-9]*\b//' conf/oj.sql
for TABLE in "${AI_TABLES[@]}"; do
  echo >> conf/oj.sql
  echo "ALTER TABLE ${TABLE} AUTO_INCREMENT=1000;" >> conf/oj.sql
done

echo >> conf/oj.sql
echo "INSERT INTO \`user\` VALUES ('1000', '0', 'root', '\$2a\$10\$lyKeLNMNYC6eXhmTb6CMb.NvtMS1SfQTIZRCddnoes6sGfk4gwsQS', null, null, 'admin@local.host', 'admin@local.host', '0', '0', null, '0', '0', '0', '0', '0', '0', '0', '127.0.0.1', null, null, null, 'secret', null, '118', '0', '', null, '1', null);" >> conf/oj.sql
echo "INSERT INTO \`user_ext\` VALUES ('1000', '0', null, null, null, null, '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0');" >> conf/oj.sql
echo >> conf/oj.sql

mysqldump -u root --password=${PASSWORD} --no-create-info --extended-insert=FALSE --single-transaction oj "${TABLES[@]}" >> conf/oj.sql
sed -i "s/'emailUser','[^']*'/'emailUser',''/g;s/'emailPass','[^']*'/'emailPass',''/g" conf/oj.sql

#mysqldump -u root --password=${PASSWORD} oj | sed -e "s/\\\'/''/g" > oj-data.sql
