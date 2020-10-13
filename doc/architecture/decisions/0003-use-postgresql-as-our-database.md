# 3. Use postgresql as our database

Date: 2020-10-12

## Status

Accepted

## Context

We need to choose a database to maintain persistence. A relational approach is probably the way to go, but I don't have enough knowledge about NOSQL to know if it would fit this project better.

## Decision

We will go with Postgresql since it's the database I'm most familiar with.

## Consequences

There might be some vendor lock-in, but postgresql is robust enough that it shouldn't be a problem in the future.
