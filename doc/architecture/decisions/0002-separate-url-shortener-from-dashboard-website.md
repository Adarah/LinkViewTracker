# 2. Separate URL shortener from dashboard website

Date: 2020-10-12

## Status

Accepted

## Context

We want the domain name from the URL shortener to be independent from the dashboard visualization website. Any added tracking functionality from the URL shortener should also not affect the dashboard.

## Decision

A good way to achieve this is utilizing a microservice architecture, this way we get to maintain the separation of concerns of each subproject.

## Consequences

It is easier to alter the code for each subproject independently, as well as add new services to the overall architecture.

The downside is that this architecture incurs a larger overhead when exchanging information between services, as well as a larger overhead for the developer's understanding of the system as whole.
