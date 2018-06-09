# Changelog

## Version 1.0.5

* Update dependencies
* Fix bugs #55, #38

## Version 1.0.4

* Update dependencies
* BREAKING: Removed Retrolambda. Since this library uses Java 8 features, from v1.0.4 on, only Android gradle plugin 3.0.0 or higher is supported.

## Version 1.0.3

* Update dependencies (Play Services 10.2.1)
* Fix bug which lead to leaks when using `rxLocation.location().updates() (#26)
* BREAKING: Removed `GoogleAPIClientSingle` and replaced it with `GoogleApiClientFlowable` (#22)
* BREAKING: Replace uppercase "API" in `GoogleApiConnectionException` and `GoogleApiConnectionSuspendedException` with "Api"

## Version 1.0.2

* Update dependencies (RxJava 2.0.7, Play Services 10.2.0)
* Fix bug which made activity recognition API unusable (#24)
* Add `StatusExceptionResumeNextTransformer` to help with handlig the resolution of a `Result` yourself (#17)

## Version 1.0.1

* Add consumer ProGuard rules. (#7, #8 – thanks Evisceration)