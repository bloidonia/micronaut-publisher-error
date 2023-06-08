This shows a breaking change between 3.9.3 and 4.0.0-M3

When a client is used to connect to a controller returning a Publisher, any status exceptions are not wrapped as json, and instead the raw string of the message is returned verbatim.

The setup in this repo is for 3.9.3

### Description

There is a controller which has 2 methods.  One returns a `Publisher<String>` and the other returns a raw `String` response.  Both simply throw an HttpStatusExcepton.

Run the test [MicronautPublisherErrorSpec](src/test/groovy/micronaut/publisher/error/MicronautPublisherErrorSpec.groovy) and it will pass.

Both client methods return a Json response with the encoded error.

### Try with 4.0.0-M3

Edit [libs.versions.toml](gradle%2Flibs.versions.toml) to comment out the 3.9.3 dependencies (ie: change it to)

```toml
[versions]
#micronaut = "3.9.3"
#application-plugin = "3.7.9"
micronaut = "4.0.0-M3"
application-plugin = "4.0.0-M4"
```

And edit the imports in the [the specification](src/test/groovy/micronaut/publisher/error/MicronautPublisherErrorSpec.groovy#L16-L17) to import `jakarta.validation.constraints.NotBlank` instead of `javax.validation.constraints.NotBlank`.

Then run the test again.

The publisher test fails.

### Expected Behavior

I'm not sure if this is an issue with validation, or aop, or something else, but I believe both outputs should be the same for both return types.