# Assignment Luminis

Read the instructions in [instructions.html](instructions.html)

# Questions

## On "All transaction references should be unique"

Unique in a single file? Or over the lifespan of the service?

- Unique in a single file.

## On "The end balance needs to be validated"

I'd assume you want to check for a given start balance and mutation:

```
endBalance = startBalance + mutation
```

Is this correct?

- Yes.

## On the report

I propose to generate some structured output, for each **failed** record:

| field       |   value |
| :---------- | ------: |
| reference   | numeric |
| error_code  |  0 or 1 |
| description |    text |

This can be json or xml:

```json
{
  "failed_records": [
    {
      "reference": 186441,
      "error_code": 1,
      "description": "Incorrect end balance."
    },
    {
      "reference": 186441,
      "error_code": 0,
      "description": "reference is not unique"
    }
  ]
}
```

- this in json is fine.

## On the architecture

This data processor could be a command line tool, part of another application, or hosted as a stand-alone service somewhere. In the spirit of the assignment I would like to go with a cloud-based solution hosted on AWS.

A few possible solutions:

- AWS Lambda + S3 + SNS. Upload the file to be processed in a bucket. Lambda processes the file and drops the results in another bucket.
- A simple EC2 instance with java spring/boot.
- AWS Serverless application model, again, with lambda and S3.

What solution do you have in mind?

- This is what you, do the expert, can tell us best.

# Implementation

I've decided on a spring / boot REST application. This, together with hosting it on AWS Elastic Beanstalk, should prove quite a fun challenge. More elaborate solutions, using different parts from AWS, can evolve from here. I wrote the XML parser with the StaX library. It is included by default, and (for small XML structures) the Iterator api is easy to use. The CSV parser is even simpler (and thus also incorrect!). It should definitely be improved upon if this was to be released.

## Api

The CSV and XML files are now to be called transactions. In other words, a transaction is simply a list of records.

Two endpoints are exposed, `/transactions` and `/reports`:

| endpoint             | allowed methods |
| :------------------- | --------------: |
| `/transactions`      |    `GET`,`POST` |
| `/transactions/{id}` |           `GET` |
| `/reports`           |           `GET` |
| `/reports/{id}`      |           `GET` |

The api accepts `POST` requests to the `/transactions` endpoint with the `Content-Type` header `text/xml` or `text/csv`. The body should be the data of the file.

Whenever a file is posted to `/transactions`, a corresponding `/transactions/{id}` and `/reports/{id}` is generated, according to the validation rules in the instructions.

I spend some time to make the service discoverable through links after some data is uploaded.

## Installation and building

Run the project with

```
./gradlew bootRun
```

Build the war archive for Elastic Beanstalk with

```
./gradlew bootWar
```

## Hosting

[A demo is hosted here](http://recordvalidator-env.eba-v3mkymry.eu-west-2.elasticbeanstalk.com/transactions)

Test the api with:

```sh
curl -X POST -H 'Content-Type: text/csv' --data-binary "@src/test/data/records.csv" http://recordvalidator-env.eba-v3mkymry.eu-west-2.elasticbeanstalk.com/transactions
```

or:

```sh
curl -X POST -H 'Content-Type: text/xml' --data-binary "@src/test/data/records.xml" http://recordvalidator-env.eba-v3mkymry.eu-west-2.elasticbeanstalk.com/transactions
```
