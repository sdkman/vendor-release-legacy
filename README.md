# GVM Broker API

This is a dedicated microservice API for the purpose of tracking Candidates and Versions. It holds information about
supported Candidates and all their corresponding Versions. It also knows the default Version of each Candidate. In
addition it provides a redirect link to the binary zip file of each Candidate Version.

## Content Negotiation

The default behaviour of the API is to return plain text responses which can be consumed by the Bash client. However,
it can also return JSON responses through content negotiation. By simply adding an `Accept` header of `application/json`
to your request, you should see the results in a JSON document.
