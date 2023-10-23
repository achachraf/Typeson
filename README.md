# Typeson
![Version](https://img.shields.io/badge/Version-1.0.1-green.svg)
![Java](https://img.shields.io/badge/Java-17-blue.svg)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Typeson is a Java library for serializing and deserializing Java objects to and from JSON.
It extends the Jackson to add support for polymorphism and dynamic type resolution.

No time to read? next section is for you.

### Typeson vs. Jackson ?

A simple polymorphic serialization example using Jackson would look like this:
```java
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({  // <-- This is what breaks the extensibility
        @JsonSubTypes.Type(value = Dog.class, name = "dog"),
        @JsonSubTypes.Type(value = Cat.class, name = "cat")
}) 
public class Animal {
    private String name;
    
    // getters and setters
}

@JsonTypeName("dog")
public class Dog extends Animal {
    private String breed;
    
    // getters and setters
}

@JsonTypeName("cat")
public class Cat extends Animal {
    private int lives;
    
    // getters and setters
}

public class Main {
    public static void main(String[] args) {
        String json = "{\"type\":\"dog\",\"name\":\"Snoopy\",\"breed\":\"Beagle\"}";
        ObjectMapper mapper = new ObjectMapper();
        Animal animal = mapper.readValue(json, Animal.class);
    }
}
```

The problem with this approach is that it requires the base class to know about all its subtypes.
This is not always possible, especially when the base class is part of a library and the subtypes are defined in a different library.

Typeson solves this problem by using a different approach.
With the capability of dynamically resolving the type of an object, Typeson can serialize and deserialize objects without the need for the base class to know about its subtypes.

```java
import io.github.achachraf.typeson.domain.ElementType;
import io.github.achachraf.typeson.interlay.Typeson;

@ElementType(name = "animal", field = "type") // <-- This is what makes it extensible
public class Animal {
    private String name;
    
    // getters and setters
}

@ElementType("dog")
public class Dog extends Animal {
    private String breed;
    
    // getters and setters
}

@ElementType("cat")
public class Cat extends Animal {
    private int lives;
    
    // getters and setters
}

public class Main {
    public static void main(String[] args) {
        String json = "{\"type\":\"dog\",\"name\":\"Snoopy\",\"breed\":\"Beagle\"}";
        Typeson typeson = new Typeson();
        Animal animal = typeson.unmarshall(json, Animal.class);
    }
}
```

You want to know more? keep reading.

## Table of Contents

1. [Installation](#installation)
2. [Usage](#usage)
   1. [Serialization](#serialization)
      1. [Simple Serialization](#simple-serialization)
      2. [List Serialization](#list-serialization)
      3. [Map Serialization](#map-serialization)
      4. [Jackson Annotations](#jackson-annotations)
    2. [Deserialization](#deserialization)
        1. [Simple Deserialization](#simple-deserialization)
        2. [List Deserialization](#list-deserialization)
        3. [Map Deserialization](#map-deserialization)
        4. [Jackson Annotations](#jackson-annotations)
    3. [Customization](#customization)
3. [Why Typeson?](#why-typeson)
4. [How does it work?](#how-does-it-work)
5. [Building from source](#building-from-source)
6. [Tests](#tests)
7. [Contributing](#contributing)
8. [License](#license)

## Installation

### Maven

**Typeson** is available on Maven Central.

```xml
<dependency>
  <groupId>io.github.achachraf</groupId>
  <artifactId>typeson</artifactId>
  <version>LATEST</version> <!-- Replace LATEST with the latest version -->
</dependency>
```

To use the Jackson features (see next sections), you will need to add the jackson module as provided dependency.

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.X.X</version> <!-- Replace X with the latest version -->
    <scope>provided</scope>
</dependency>
```

## Usage

We will use the following classes for the examples:

```java
import io.github.achachraf.typeson.domain.ElementType;

@ElementType(name = "anime", field = "type")
public class Animal {
    private String name;
    
    // getters and setters
}

@ElementType("dog")
public class Dog extends Animal {
    private String breed;
    
    // getters and setters
}

@ElementType("cat")
public class Cat extends Animal {
    private int lives;
    
    // getters and setters
}
```

> IMPORTANT: Typeson uses getters and setters to access the fields of the objects. If not provided, the fields will be ignored.

### Serialization

#### Simple Serialization

```java

public class Main {
    public static void main(String[] args) {
        Animal animal = new Dog();
        animal.setName("Snoopy");
        ((Dog) animal).setBreed("Beagle");
        Typeson typeson = new Typeson();
        String json = typeson.marshall(animal); 
        // output: {"type":"dog","name":"Snoopy","breed":"Beagle"}
    }
}
```

If the "field" attribute is not specified in the `@ElementType` annotation, the default value is "TYPESON_FIELD_TYPE".

```java
@ElementType(name = "animal")
public class Animal {
    public String name;
}

// ...

public class Main {
    public static void main(String[] args) {
        Animal animal = new Dog();
        // ...
        String json = typeson.serialize(animal); 
    }
}
```

Output:

```json
{"TYPESON_FIELD_TYPE":"dog","name":"Snoopy","breed":"Beagle"}
```

> The "name" attribute is mandatory in the `@ElementType` annotation. If not specified, an exception will be thrown.

#### List Serialization

```java
import java.util.ArrayList;
import java.util.List;
import io.github.achachraf.typeson.interlay.Typeson;

public class Main {
    public static void main(String[] args) {
        List<Animal> animals = new ArrayList<>();
        animals.add(new Dog());
        animals.add(new Cat());
        Typeson typeson = new Typeson();
        String json = typeson.marshall(animals); 
    }
}
```

Output:

```json
[{"type":"dog"},{"type":"cat"}]
```

#### Map Serialization

Not supported yet.

#### Jackson Annotations

Typeson keep support for all the Jackson annotations for the serialization.

```java
import com.fasterxml.jackson.annotation.JsonProperty;

// ...

@ElementType("specificDog")
public class SpecificDog extends Dog {
    @JsonProperty("dog_breed")
    public String breed;
}

// ...

public class Main {
    public static void main(String[] args) {
        Animal animal = new SpecificDog();
        animal.setName("Snoopy");
        ((SpecificDog) animal).setBreed("Beagle");
        Typeson typeson = new Typeson();
        String json = typeson.marshall(animal); 
    }
}
```

Output:

```json
{"type":"specificDog","name":"Snoopy","dog_breed":"Beagle"}
```

### Deserialization

Unlike the serialization, the deserialization provides 3 APIs:

- `unmarshall(String json, Class<T> clazz): T` for single object deserialization.
- `unmarshallList(String json, Class<T> clazz): List<T>` for list deserialization.
- `unmarshallList(String json, TypeReference<T> typeReference): T` for list deserialization with complex types.


#### Simple Deserialization

```java

public class Main {
    public static void main(String[] args) {
        String json = "{\"type\":\"dog\",\"name\":\"Snoopy\",\"breed\":\"Beagle\"}";
        Typeson typeson = new Typeson();
        Animal animal = typeson.unmarshall(json, Animal.class);
        assert animal instanceof Dog; // true
    }
}
```

If the "Dog" class does not have the `@ElementType` annotation, the json will be deserialized to the "Animal" class.

```java

public class Main {
    public static void main(String[] args) {
        String json = "{\"type\":\"dog\",\"name\":\"Snoopy\",\"breed\":\"Beagle\"}";
        Typeson typeson = new Typeson();
        Animal animal = typeson.unmarshall(json, Animal.class);
        assert animal instanceof Animal; // true
    }
}
```


#### List Deserialization

We will use the following JSON for the examples:

```json
[
  {
    "type": "dog",
    "name": "Snoopy",
    "breed": "Beagle"
  },
  {
    "type": "cat",
    "name": "Garfield",
    "lives": 9
  }
]
```

```java
import java.util.List;
import io.github.achachraf.typeson.interlay.Typeson;

public class Main {
    public static void main(String[] args) {
        String json = "above json";
        Typeson typeson = new Typeson();
        List<Animal> animals = typeson.unmarshallList(json, Animal.class);
    }
}
```

If having more advanced types, you can use the `TypeReference` class:

```json
[
 [
    {
      "type": "dog",
      "name": "Snoopy",
      "breed": "Beagle"
    },
    {
      "type": "cat",
      "name": "Garfield",
      "lives": 9
    }
     ],
     [
    {
      "type": "dog",
      "name": "Snoopy",
      "breed": "Beagle"
    },
    {
      "type": "cat",
      "name": "Garfield",
      "lives": 9
    }
 ]
]
```

```java

public class Main {
    public static void main(String[] args) {
        String json = "above json";
        Typeson typeson = new Typeson();
        List<List<Animal>> animals = typeson.unmarshallList(json, new TypeReference<List<List<Animal>>>() {});
    }
}
```

#### Map Deserialization

Not supported yet.

#### Jackson Annotations

Typeson keep support for the main Jackson annotations for the deserialization:

- `@JsonProperty` 
- `@JsonAlias`
- `@JsonIgnore`

```java

public class Main {
    public static void main(String[] args) {
        String json = "{\"type\":\"specificDog\",\"name\":\"Snoopy\",\"dog_breed\":\"Beagle\"}";
        Typeson typeson = new Typeson();
        Animal animal = typeson.unmarshall(json, Animal.class);
        assert animal instanceof SpecificDog; // true
        assert ((SpecificDog) animal).getBreed().equals("Beagle"); // true
    }
}
```

If you think there is a missing annotation, please open an issue.

### Customization

The customization is only available for the deserialization with these 2 properties:

- FAIL_ON_NULL_FOR_PRIMITIVES, default: false
- FAIL_ON_UNKNOWN_PROPERTIES, default: true

The `Typeson` class provides 2 constructors:

- `Typeson()` for the default configuration.
- `Typeson(ConfigProvider)` for the custom configuration.

A `ConfigProvider` is a functional interface that provides the configuration:

```java
public interface ConfigProvider {
    
    boolean getProperty(String key);
}
```

A default implementation is provided with a HashMap that could be reused for the customization:

```java

import io.github.achachraf.typeson.interlay.ConfigProviderMap;

public class Main {
    public static void main(String[] args) {
        Typeson typeson = new Typeson(
                new ConfigProviderMap()
                        .setProperty(ConfigProviderMap.FAIL_ON_NULL_FOR_PRIMITIVES, true)
                        .setProperty(ConfigProviderMap.FAIL_ON_UNKNOWN_PROPERTIES, false)
        );
        // ...
    }
}
```

## Why Typeson?

As shown in the first example, the serialization of the polymorphic objects is not easy with the Jackson library, as it requires the parent class to know all the child classes.
This breaks one of the main principles of the object-oriented programming: _**the OCP (Open-Closed Principle)**_.

In addition, it could also break many other principles like the _**LSP (Liskov Substitution Principle)**_ and the _**SRP (Single Responsibility Principle)**_.

The Typeson library provides a solution for this problem by using the `@ElementType` annotation and an advanced ClassPath scanning for the deserialization.

## How does it work?

The main innovation part in Typeson is the deserialization, as the serialization is just a simple Jackson serialization with the type field injection.

The deserialization is based on the `@ElementType` annotation and the ClassPath scanning with the [ClassGraph](https://github.com/classgraph/classgraph) library.

The library scans the classpath for the classes that have the `@ElementType` annotation, and resolve the type of the object by using the `type` field in the JSON.

For optimization, the library uses a cache for the scanned classes, so the scanning will be done only once per execution.


## Building from source

### Requirements

- JDK 17 or higher
- Maven 3.8.2 or higher
- Git

To build, test and package the project as JAR :

```bash
git clone https://github.com/achachraf/Typeson.git
cd Typeson
mvn clean package
```

JAR file and javadoc will be available in target/ directory. To make it available to any other Maven project on your machine :

```bash
mvn install
```

## Tests

The tests are written with JUnit 5. To run the tests:

```bash
mvn test
```

Typeson also provides mutation tests with [PIT](https://pitest.org/). To run the mutation tests:

```bash
mvn test-compile org.pitest:pitest-maven:mutationCoverage
```

A report will be generated in target/pit-reports/yyyyMMddhhmm/index.html

Test also provides some interesting examples of the library usage.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Long Live Open Source ❤️

## License

Copyright 2023-2024 Achraf ACHKARI-BEGDOURI.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an " AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.