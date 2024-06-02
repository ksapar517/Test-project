

http://localhost:8082/auth/register

{
"username":"Sapar",
"name": "Sapar",
"surName": "Kutbidinov",
"email": "ksapar5171@gmail.com",
"telNumber": "+13333456417889",
"birthdate": "1990-01-01",
"password": "12345678"
}

парол min 6 max 8 символы

http://localhost:8082/auth/login

{
"username": "Sapar",
"password": "12345678"
}

http://localhost:8082/auth/addBalace

{
"shotName": "100002",
"balance": 30000
}


http://localhost:8082/bank/moneyTransfer
{
"shotName": "100002",
"balance": 30000
}

http://localhost:8082/bank/moneyTransfer
{
"email":"bakhodir312@gmail.com"
}

http://localhost:8082/user/changeinfo
{
"email":"kutbidinov@gmail.com"
}

http://localhost:8082/user/delete
{
"email":"delete"
}

http://localhost:8082/user/changePassword
{
"password": "111111111"
}






