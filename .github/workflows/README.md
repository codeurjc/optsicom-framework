# Release

## Versioning

This project is versioned and published using the plugins [Versions Maven Plugin](https://www.mojohaus.org/versions-maven-plugin/index.html) and [Maven Release Plugin](https://maven.apache.org/maven-release/maven-release-plugin/).

After a release, during the development phase the version of the  projects will be advanced and the -SNAPSHOT suffix will be added.

To do this through the plugin you can launch the following command from the parent project:

```bash
mvn versions:set -DnewVersion=*.*.*-SNAPSHOT
```

## Publish

You can release manually using the plugin [Maven Release Plugin](https://maven.apache.org/maven-release/maven-release-plugin/) or use the Github Actions workflow: "Release Maven".

When launching the workflow you have to add the version to be released as an input variable. The project should be in the SNAPSHOT version corresponding to the release to be performed. The workflow will automatically change the project version and upload it to Github.