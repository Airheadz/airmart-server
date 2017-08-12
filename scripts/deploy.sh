#!/usr/bin/env bash

cp airmart-example.yml airmart.yml
sed -i -e "s/CALLBACK/$CALLBACK/g" airmart.yml
sed -i -e "s/CLIENT_ID/$CLIENT_ID/g" airmart.yml
sed -i -e "s/CLIENT_SECRET/$CLIENT_SECRET/g" airmart.yml
sed -i -e "s/DISCORD_USER_AGENT/$DISCORD_USER_AGENT/g" airmart.yml
sed -i -e "s/DISCORD_WEBHOOK_URL/$DISCORD_WEBHOOK_URL/g" airmart.yml
sed -i -e "s/FITTINGS_BASE_PATH/$FITTINGS_BASE_PATH/g" airmart.yml
sed -i -e "s/SCOPES/$SCOPES/g" airmart.yml

scp build/libs/airmart-shadow.jar aiko@164.132.230.218:/opt/airheadz/airmart/airmart.jar
scp airmart.yml aiko@164.132.230.218:/opt/airheadz/airmart/airmart.yml
