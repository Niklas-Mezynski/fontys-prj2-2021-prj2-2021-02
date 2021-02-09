# Plantuml as tool for modeling in UML

## UML
∏
UML means Unified Modeling Language and is a specification of a modeling language. 

The specification can be found here 
- [About the Unified Modeling Language Specification Version 2.5.1](https://www.omg.org/spec/UML/About-UML/)

For modeling UML there are a lot of tools available from simple online drawing tools up to enterprise tools for software architects with code generation and a lot more integrated functionalities.

##  PlantUML

However, more and more companies and open source projects make use of lightweight software. For educational purposes it also makes sense to keep things simple to support the learning process and not to be an expert in a special software tool or just learn usage of a specific tool.

Therefore [PlantUML](https://plantuml.com/) is the tool of choice as it allows to create models based on simple text, thus allows to use version control as with code on all models.

## Set up

To set up the environment it is recommended to use a simple editor. The preferred editor of choice and where we support is [Visual Studio Code ](https://code.visualstudio.com/) with the PlantUML Plugin (extension).

So do the following steps:

- Download and install [Visual Studio Code ](https://code.visualstudio.com/)
- Start Visual Studio Code
- install the PlantUML plugin

![Visual Studio Code - Extensions](resources/vs-01.jpg)

![Visual Studio Code - PlantUML](resources/vs-02.jpg)

- Create a new file, call it test.puml
- fill in the following example code:

```plantuml
@startuml test
skinparam classAttributeIconSize 0
skinparam monochrome true
skinparam componentStyle uml2
hide circle

class Driver {
    name
}

class Car {
    brand
    type
}

class Person {
    name
}

class Wheel {
    brand
}

Driver - Car : drives >
Car -right- Wheel : have  >
Car -- Person : < owns


@enduml
```

- now press **Alt-d** to generate PlantUML Preview. 
- Additional functionalities like generate an SVG or PNG file can be found via the command palette (Menu > View > Command Palette)

![Start command palette](resources/vs-03.jpg)

![Type plantuml in command palette](resources/vs-04.jpg)


## UML models in PlantUML

See [Language specification pages](https://plantuml.com/sitemap-language-specification) and then choose diagram type for example and explanations of the syntax.

### Examples

#### Use Case diagram

```plantuml
@startuml
left to right direction
skinparam classAttributeIconSize 0
skinparam monochrome true
skinparam componentStyle uml2
hide circle
actor "Food Critic" as fc
rectangle Restaurant {
  usecase "Eat Food" as UC1
  usecase "Pay for Food" as UC2
  usecase "Drink" as UC3
}
fc --> UC1
fc --> UC2
fc --> UC3
@enduml
```

#### Activity Diagram

```plantuml
@startuml
skinparam classAttributeIconSize 0
skinparam monochrome true
skinparam componentStyle uml2
hide circle

(*) --> ===B1===
--> "Parallel Action 1"
--> ===B2===

===B1=== --> "Parallel Action 2"
--> ===B2===

--> (*)

@enduml
```

#### Class diagram

```plantuml
skinparam classAttributeIconSize 0
skinparam monochrome true
skinparam componentStyle uml2
hide circle

class ATM {
    -addTransaction(atm:ATMTransaction)
    +withdraw(card:BankCard, amount:int)
    -findBank(card:BankCard):Bank
    -makeMoneyAvailable()
}

class Customer {
    -name:String
}

class BankCard {
    -cardNumber:int
}

class Account {
    -accountNumber:int
    -withDraw(amount:int)
}

class ATMTransaction {
    ATMTransaction(atm:ATM, card:BankCard,amount:int)
}

class Bank {
    -findAccount(card:BankCard):Account
    +withDraw(atm:ATM, card:BankCard,amount:int):ATMTransaction
}

ATM "1" -down-> "*" ATMTransaction
ATMTransaction "*" -right-> "1" BankCard
Account "*" -up-> "1" Customer
Account "1" -left-> "*" BankCard
Bank "1" -up-> "*" Account
Bank -.up.-> ATMTransaction : <<creates>>

```

#### Sequence diagram 

```plantuml
skinparam classAttributeIconSize 0
skinparam monochrome true
skinparam componentStyle uml2
hide circle
hide footbox

actor User

User-> A: DoWork

activate A

A -> A: Internal call
activate A

create B
A --> B : << create >>

A -> B : doSomething()
activate B
A<--B : returnSomething
deactivate B
deactivate A
User<- A: Done
deactivate A
```

#### State Machine Diagram

```plantuml
@startuml

skinparam state {
    FontColor          white
    AttributeFontColor white
    FontSize           17
    AttributeFontSize  15
    AttributeFontname  Droid Sans Mono
    BackgroundColor    #527BC6
    BorderColor        black
    ArrowColor         #222266
}

[*] --> Unit : juju deploy <i>or</i>\njuju add-unit
state Unit {
    [*] --> new
    new --> ready : /<b>install</b> hook [exit 0]
    new --> install_error : [error-install]
    ready --> running : /<b>start</b> hook [exit 0]
    ready --> start_error : [error-start]
    state running {
        [*] --> unexposed
        unexposed --> exposed : juju expose\n/<b>exposed</b> hook
        exposed --> unexposed : juju unexpose\n/<b>unexposed</b> hook

        --

        [*] --> config : [first startup]\n/<b>config-changed</b> hook
        config --> config : juju set\n/<b>config-changed</b> hook
    }
    running --> ready : /<b>stop</b> hook [exit 0]
    running --> stop_error : [error-stop]

    install_error --> ready : juju resolved
    install_error --> ready : juju resolved --retry\n/<b>install</b> hook [exit 0]

    start_error --> running : juju resolved
    start_error --> running : juju resolved --retry\n/<b>start</b> hook [exit 0]

    stop_error --> ready : juju resolved
    stop_error --> ready : juju resolved --retry\n/<b>stop</b> hook [exit 0]
}

Unit --> [*] : juju remove-unit <i>or</i>\njuju destroy-service <i>or</i>\njuju destroy-environment

@enduml
```
#### Component diagram

```plantuml
@startuml
skinparam classAttributeIconSize 0
skinparam monochrome true
skinparam componentStyle uml2
hide circle
title HelpMeNow

package "HelpMeNow" {
    [Frontend] --0)- [Interface]
    [Frontend] --(0- [Backend]
}

@enduml
```

#### Deployment diagram

```plantuml
@startuml
skinparam classAttributeIconSize 0
skinparam monochrome true
skinparam componentStyle uml2
hide circle

node frontend
node backend
database database
frontend --> backend : <<https>>
backend --> database : <<jdbc>>

@enduml
```

#### UI-Design

```plantuml
@startsalt
{+
{* File | Edit | Source | Refactor
 Refactor | New | Open File | - | Close | Close All }
{/ General | Fullscreen | Behavior | Saving }
{
{ Open image in: | ^Smart Mode^ }
[X] Smooth images when zoomed
[X] Confirm image deletion
[ ] Show hidden images
}
[Close]
}
@endsalt
```

#### WBS

```plantuml
@startwbs
* 1. Intranet project
** 1.1 Concept
*** 1.1.1 Evaluate current systems
*** 1.1.2 Define requirements
**** 1.1.2.1 Define user requirements
**** 1.1.2.2 Define content requirements
**** 1.1.2.3 Define system requirements
**** 1.1.2.4 Define server owner requirements
*** 1.1.3 Define specific functionality
*** 1.1.4 Define risks and risk management approach
*** 1.1.5 Develop project plan
*** 1.1.6 Brief web development team
** 1.2 Web site Design
** 1.3 Web site development
** 1.4 Roll out
** 1.5 Support
@endwbs
```


#### GANTT-Chart

```plantuml
@startgantt
Project starts 2020-07-01
printscale weekly

[1.1.1 Complete Stakeholder Research] lasts 14 days
then [1.1.2 Initial Implementation Plan] lasts 14 days
then [1.2.1 Model of AsIs Processes Completed] lasts 5 days
then [1.2.1 Model of AsIs Processes Completed] lasts 4 days
then [1.2.1.2 Model of AsIs Processes Completed2] lasts 30 days

[1.1.1 Complete Stakeholder Research] is colored in GreenYellow/Green
[1.1.2 Complete Stakeholder Research] is colored in GreenYellow/Green

[EndOfProject] happens at [1.2.1.2 Model of AsIs Processes Completed2]'s end

@endgantt
```

#### Use Graphviz Dot language

```plantuml
@startuml
digraph foo {
  node [style=rounded]
  node1 [shape=box]
  node2 [fillcolor=yellow, style="rounded,filled", shape=diamond]
  node3 [shape=record, label="{ a | b | c }"]

  node1 -> node2 -> node3
}
@enduml
```

For more examples, see [Language specification pages](https://plantuml.com/sitemap-language-specification).


## Embed plantuml code in your markdown file

If you look into the source code of this file, you see that planuml code is embedded into markdown. In visual studio code preview, the plantuml picture will be automatically generated.

It looks like:

![plantuml embedded in markdown](resources/99-example-01.jpg)

Embed in your markdown the planuml code as follows:

```plantuml
@startuml
left to right direction
skinparam classAttributeIconSize 0
skinparam monochrome true
skinparam componentStyle uml2
hide circle
actor "Food Critic" as fc
rectangle Restaurant {
  usecase "Eat Food" as UC1
  usecase "Pay for Food" as UC2
  usecase "Drink" as UC3
}
fc --> UC1
fc --> UC2
fc --> UC3
@enduml
```