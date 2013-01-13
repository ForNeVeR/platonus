platonus
========
platonus is simple Markov network management tool.

## Usage

The latest version has the GUI. To run it, execute the following commands in
your terminal:

    cd platonus
    lein run

## Testing

Use the following command to run test suite:

    lein test

## Library

### Artifacts

Here is maven artifact identifier (it is published in the [clojars](https://clojars.org/) repository):

    <dependency>
      <groupId>platonus</groupId>
      <artifactId>platonus</artifactId>
      <version>0.1.16</version>
    </dependency>

### Java API

platonus API is deadly simple.

#### Network class

1. `Network.addPhrase` adds phrase to the network.
2. `Network.generate` generates random network.
3. `Network.diff` calculates the difference between two networks.

#### Filesystem class

`Filesystem.scanDirectory` scans the directory and generates corresponding Markov network.
