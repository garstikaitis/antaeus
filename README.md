## Introduction

I have built a service which goes through all the invoices and updates their status to either `PAID` or `REJECTED` depended on the outcome. If today is not the first day of the month it will return a message saying `Try again later` and skip whole process of updating invoices statuses

## Technology constraints

After analyzing the codebase, I saw that Dockerfile is present. 'Cool' I thought, I don't need to download all necessary JDKs. After running docker script, everything worked like charm but I wanted to take it one step further and run everything locally. After spending few hours researching, I was finally able to run repository locally. Tests were running, debugger was working. `Sweet`
The solution was built using IntelIJ Community edition on MacOS.

## Process

After reading the challenge and analyzing initial codebase, I saw that it is written in Kotlin. Since, I have no experience with Kotlin, I decided that first day of the challenge I will just focus on reading documentation/blog posts about Kotlin, write some Hello World applications. At the end of the first day, I was able to write some basic Kotlin code, and made the first commit to repository - Getting invoices wiith status of `PENDING` was working like a charm. That was a good progress for one day, and I decided that challenge will be solved on the upcoming day.

The next day I decided to start with sketching out the solution on paper. Outcome of it was as follows:

```
├── Check if today is the first day of the month
├──── if today is the first day
├────── Get all invoices with the status of Pending
├────── Run charge() which is responsible for contact 3rd party API which is supposed to charge customer
├──────── Check if customer's and invoices's currencies are the same
├────────── if true update invoice status to PAID
├────────── else throw exception <CurrencyMismatchException>
├──────── If customer balance is negative
├────────── update invoice status to REJECTED
├────────── Return false (customer cannot pay right now)
├──── if today is not the first day
├──── return response stating 'Try again later'
```

Once I had logic in place I started implementing it.
Once `/billing` endpoint is visited, BillingService method `run()` is being triggered. It acts as an entry point for the whole billing cycle. This method is getting all invoices with status `PENDING` and runs charge method on them. In the end response is returned containing all the data to be consumed by other parties. `charge()` method does effects that are explained in the pseudo code above.

## Frontend

For this challenge I decided to go with Full stack solution and built a dashboard which presents all the invoices, followed with some mocked customer data. Dashboard also has a button which charges all the customers. In other words just interacts with `/billing` endpoint. Solution is built with React, with only one extra dependency - `axios - HTTP client`. All the data between components is flowing by using simplest approach - `Props goes down, events go up`. I decided to not use any other state management library because that would be overkill for application like this.

## How to run frontend solution

1. Navigate to frontend/pleo folder
2. run `npm install` (Assuming that node.js is installed locally)
3. run `npm run start`

## Issues on running solution through docker

After solution was done, I was not able to run my solution through Docker anymore, it fails on `test` command. My guess it is because I ran tests through IntelIJ and now there is different versioning with my local Gradle version and version included in Docker.

## Improvements

My proposed solution could be solved in many different ways. This solution includes very simple approach, where every invoice is getting processed in sequential fashion. Another thing that could be improved is to add asynchronous process when updating invoices. I looked into that and I saw that Kotlin supports `Coroutines`, so that could be a definate improvement

## Conclusion

To conclude my experience of solving this challenge I would say that I had very pleasant experience, despite the fact that After reading a challenge description, I thought 'what the hell 😅', I applied as a Frontend developer and the task is backend. I never worked with with Java, nor with Kotlin but I feel that it is a very developer friendly and powerful language to build applications at any scale.

## Antaeus

Antaeus (/ænˈtiːəs/), in Greek mythology, a giant of Libya, the son of the sea god Poseidon and the Earth goddess Gaia. He compelled all strangers who were passing through the country to wrestle with him. Whenever Antaeus touched the Earth (his mother), his strength was renewed, so that even if thrown to the ground, he was invincible. Heracles, in combat with him, discovered the source of his strength and, lifting him up from Earth, crushed him to death.

Welcome to our challenge.

## The challenge

As most "Software as a Service" (SaaS) companies, Pleo needs to charge a subscription fee every month. Our database contains a few invoices for the different markets in which we operate. Your task is to build the logic that will pay those invoices on the first of the month. While this may seem simple, there is space for some decisions to be taken and you will be expected to justify them.

### Structure

The code given is structured as follows. Feel free however to modify the structure to fit your needs.

```
├── pleo-antaeus-app
|
|       Packages containing the main() application.
|       This is where all the dependencies are instantiated.
|
├── pleo-antaeus-core
|
|       This is where you will introduce most of your new code.
|       Pay attention to the PaymentProvider and BillingService class.
|
├── pleo-antaeus-data
|
|       Module interfacing with the database. Contains the models, mappings and access layer.
|
├── pleo-antaeus-models
|
|       Definition of models used throughout the application.
|
├── pleo-antaeus-rest
|
|        Entry point for REST API. This is where the routes are defined.
└──
```

## Instructions

Fork this repo with your solution. We want to see your progression through commits (don’t commit the entire solution in 1 step) and don't forget to create a README.md to explain your thought process.

Happy hacking 😁!

## How to run

```
./docker-start.sh
```

## Libraries currently in use

- [Exposed](https://github.com/JetBrains/Exposed) - DSL for type-safe SQL
- [Javalin](https://javalin.io/) - Simple web framework (for REST)
- [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) - Simple logging framework for Kotlin
- [JUnit 5](https://junit.org/junit5/) - Testing framework
- [Mockk](https://mockk.io/) - Mocking library
