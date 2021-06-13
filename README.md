# CurrencyConverter
Currency converter microservice based on NBP Web API (http://api.nbp.pl)

## Getting Started

Prerequisites:
- JDK 11

### Running the app

1. Clone the repository
2. Import the project as a maven project into your favourite IDE (or run maven on the terminal)

4. Build the project at the root using:

`./mvnw clean install` (Unix); 
or `mvnw.cmd clean install` (Windows); 
or use built in tools;

## Use the url: (http://localhost:8081/swagger-ui/)
to comfortably familiarize yourself with the functionality

Or:

### Use GET method with url: `http://localhost:8081/`
to get all currencies

### Use POST method with url: `http://localhost:8081/buy`
with example body:

    {
        "code": "AUD",
        "value": 10
    }
where `code` is the code of the currency in which the purchase will be made

and `value` is the amount of default currency

### Use POST method with url: `http://localhost:8081/sale`
with example body:

    {
        "code": "AUD",
        "value": 10
    }
where `code` is the code of the currency to be sold

and `value` is the amount of the currency to be sold


### use POST method with url: `http://localhost:8081/convert`
with example body:

    {
        "currencyDTO": {
            "code": "USD",
            "value": 10
        },
        "targetCode": "AUD"
    }
where `currencyDTO` is the convertible currency

and `targetCode` is the code of the currency in which the purchase will be made
