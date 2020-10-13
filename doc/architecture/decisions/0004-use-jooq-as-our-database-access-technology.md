# 4. Use Jooq as our database access technology

Date: 2020-10-12

## Status

Accepted

## Context

ORMs obfuscate the underlying SQL they generate and can often prove to be more harm than help to developers. 

## Decision

We will use Jooq as our database access technology.

## Consequences

Jooq is a relatively known query builder in Java, but unlike using raw SQL in JDBC, it allows for error detection during compile-time.
Unlike ORMs, using Jooq to its full potential does not allow us to compltely abstract the database being used, as certain methods are only available in specific database drivers.
