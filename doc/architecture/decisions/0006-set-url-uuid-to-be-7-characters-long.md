# 6. Set url uuid to be 7 characters long

Date: 2020-10-12

## Status

Accepted

## Context

We need to decide on the length of characters our URL shortener will use.

## Decision

We will use 7 characters. This number was arbitrarely chosen by copying bitly's decision, and it allows for over 300 billion different URLs, far more than we will ever need.

## Consequences

Changing the number to be larger/smaller might require a change in a database schema.
