# 7. Use shared secret key between services

Date: 2020-10-12

## Status

Accepted

## Context

We need to make sure only our authenticated users utilizing our dashboard service are capable of acessing the information on the nuber of times each of their URLs was clicked. The URL shortner will not have any concept of a "user", so implementing a login/role system in it is out of question.

## Decision

We will use a shared secret between all our services.

## Consequences

The usage of a single key simplifies the development of the application.
If this secret is leaked, the entire application might be compromised, incluing other microservices.
