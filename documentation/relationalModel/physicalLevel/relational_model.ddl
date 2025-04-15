CREATE SEQUENCE seq_PartReservation;


CREATE TABLE ClientType (
    ClientTypeID char(1) NOT NULL CHECK(ClientTypeID IN ('I', 'C')),
    ClientTypeName varchar2(10) NOT NULL UNIQUE CHECK(ClientTypeName IN ('Individual', 'Company')),
    PRIMARY KEY (ClientTypeID));

CREATE TABLE ClientState (
    ClientStateID char(1) NOT NULL CHECK(ClientStateID IN ('A', 'I')),
    ClientStateName varchar2(8) NOT NULL UNIQUE CHECK(ClientStateName IN ('Active', 'Inactive')),
    PRIMARY KEY (ClientStateID));

CREATE TABLE ProductOrder (
    OrderID number(10) NOT NULL,
    ProductID varchar2(10) NOT NULL,
    ProductQuantity number(5) NOT NULL,
    PRIMARY KEY (OrderID, ProductID));

CREATE TABLE Product (
    ProductID varchar2(10) NOT NULL,
    ProductFamilyID number(5) NOT NULL,
    ProductName varchar2(50) NOT NULL,
    Capacity number(4),
    "Size" number(8, 4),
    Color varchar2(60),
    AdditionalRemarks varchar2(255),
    PRIMARY KEY (ProductID));

CREATE TABLE Part (
    PartID varchar2(10) NOT NULL,
    Description varchar2(255) NOT NULL UNIQUE,
    PRIMARY KEY (PartID));

CREATE TABLE BOO (
    ProductID varchar2(10) NOT NULL,
    NumOperations number(4),
    PRIMARY KEY (ProductID));

CREATE TABLE ProductBOOOperation (
    ProductID varchar2(10) NOT NULL,
    OperationID number(10) NOT NULL,
    OperationNumber number(5) NOT NULL,
    NextOperation varchar2(10) DEFAULT 'None' NOT NULL,
    PRIMARY KEY (ProductID, OperationID, OperationNumber));

CREATE TABLE Operation (
    OperationID number(10) NOT NULL,
    OperationTypeID number(10) NOT NULL,
    ExpectedExecutionTime number(5) NOT NULL,
    PRIMARY KEY (OperationID));

CREATE TABLE IntermediateProduct (
    IntermediateID varchar2(10) NOT NULL,
    PRIMARY KEY (IntermediateID));

CREATE TABLE SupplyOffer (
    SupplierID number(6) NOT NULL,
    StockPartID varchar2(10) NOT NULL,
    StartDate date NOT NULL,
    EndDate date,
    MinimumQuantity number(5) NOT NULL,
    UnitCost number(9) NOT NULL,
    PRIMARY KEY (SupplierID, StockPartID, StartDate));

CREATE TABLE Supplier (
    SupplierID number(6) NOT NULL,
    Name varchar2(132) NOT NULL,
    PhoneNumber number(14) NOT NULL,
    EmailAddress varchar2(254) NOT NULL,
    PRIMARY KEY (SupplierID));

CREATE TABLE Component (
    ComponentID varchar2(10) NOT NULL,
    PRIMARY KEY (ComponentID));

CREATE TABLE OperationInput (
    OperationID number(10) NOT NULL,
    PartID varchar2(10) NOT NULL,
    Quantity number(4) NOT NULL,
    PRIMARY KEY (OperationID, PartID));

CREATE TABLE OperationOutput (
    OperationID number(10) NOT NULL,
    Quantity number(4) NOT NULL,
    ManufacturedPartID varchar2(10) NOT NULL,
    PRIMARY KEY (OperationID));

CREATE TABLE OperationType (
    OperationTypeID number(10) NOT NULL,
    OperationTypeName varchar2(255) NOT NULL UNIQUE,
    PRIMARY KEY (OperationTypeID));

CREATE TABLE CanBeDoneAt (
    OperationTypeID number(10) NOT NULL,
    WorkstationTypeID varchar2(5) NOT NULL,
    MaximumExecutionTime number(5) NOT NULL,
    SetupTime number(5) NOT NULL,
    PRIMARY KEY (OperationTypeID, WorkstationTypeID));

CREATE TABLE WorkstationType (
    WorkstationTypeID varchar2(5) NOT NULL,
    WorkstationTypeName varchar2(255) NOT NULL UNIQUE,
    PRIMARY KEY (WorkstationTypeID));

CREATE TABLE "Order" (
    OrderID number(10) NOT NULL,
    ClientID number(6) NOT NULL,
    DeliveryAddressID number(10) NOT NULL,
    OrderDate date NOT NULL,
    DeliveryDate date NOT NULL,
    PRIMARY KEY (OrderID));

CREATE TABLE ProductFamily (
    ProductFamilyID number(5) NOT NULL,
    ProductFamilyName varchar2(80) NOT NULL UNIQUE,
    PRIMARY KEY (ProductFamilyID));

CREATE TABLE Client (
    ClientID number(6) NOT NULL,
    ClientTypeID char(1) NOT NULL,
    ClientStateID char(1) DEFAULT 'A' NOT NULL,
    AddressID number(10) NOT NULL,
    Name varchar2(132) NOT NULL,
    VATIN varchar2(15) NOT NULL UNIQUE,
    PhoneNumber number(14) NOT NULL,
    EmailAddress varchar2(254) NOT NULL,
    PRIMARY KEY (ClientID));

CREATE TABLE Workstation (
    WorkstationID number(5) NOT NULL,
    WorkstationTypeID varchar2(5) NOT NULL,
    WorkstationName varchar2(50) NOT NULL UNIQUE,
    Description varchar2(255) NOT NULL,
    PRIMARY KEY (WorkstationID));

CREATE TABLE StockPart (
    StockPartID varchar2(10) NOT NULL,
    CurrentStock number(6) NOT NULL,
    MinimumStock number(6) NOT NULL,
    PRIMARY KEY (StockPartID));

CREATE TABLE RawMaterial (
    RawMaterialID varchar2(10) NOT NULL,
    UnitID number(3) NOT NULL,
    PRIMARY KEY (RawMaterialID));

CREATE TABLE Address (
    AddressID number(10) NOT NULL,
    Street varchar2(255) NOT NULL,
    ZipCode varchar2(10) NOT NULL,
    Town varchar2(50) NOT NULL,
    Country varchar2(60) NOT NULL,
    PRIMARY KEY (AddressID));

CREATE TABLE RawMaterialUnit (
    UnitID number(3) NOT NULL,
    UnitName varchar2(100) NOT NULL,
    Symbol varchar2(10) NOT NULL,
    PRIMARY KEY (UnitID));

CREATE TABLE ManufacturedPart (
    ManufacturedPartID varchar2(10) NOT NULL,
    PRIMARY KEY (ManufacturedPartID));

CREATE TABLE PartReservation (
    ReservationID number(10) NOT NULL,
    OrderID number(10) NOT NULL,
    StockPartID varchar2(10) NOT NULL,
    ReservedQuantity number(5) NOT NULL,
    ReservationDate date NOT NULL,
    PRIMARY KEY (ReservationID));


ALTER TABLE Client ADD CONSTRAINT FKClient160128 FOREIGN KEY (ClientTypeID) REFERENCES ClientType (ClientTypeID);

ALTER TABLE Client ADD CONSTRAINT FKClient568975 FOREIGN KEY (ClientStateID) REFERENCES ClientState (ClientStateID);

ALTER TABLE Client ADD CONSTRAINT FKClient829449 FOREIGN KEY (AddressID) REFERENCES Address (AddressID);

ALTER TABLE "Order" ADD CONSTRAINT FKOrder976445 FOREIGN KEY (DeliveryAddressID) REFERENCES Address (AddressID);

ALTER TABLE ProductOrder ADD CONSTRAINT FKProductOrd388892 FOREIGN KEY (OrderID) REFERENCES "Order" (OrderID);

ALTER TABLE Product ADD CONSTRAINT FKProduct855344 FOREIGN KEY (ProductID) REFERENCES ManufacturedPart (ManufacturedPartID);

ALTER TABLE ProductOrder ADD CONSTRAINT FKProductOrd527714 FOREIGN KEY (ProductID) REFERENCES Product (ProductID);

ALTER TABLE BOO ADD CONSTRAINT FKBOO448646 FOREIGN KEY (ProductID) REFERENCES Product (ProductID);

ALTER TABLE ProductBOOOperation ADD CONSTRAINT FKProductBOO797536 FOREIGN KEY (ProductID) REFERENCES BOO (ProductID);

ALTER TABLE ProductBOOOperation ADD CONSTRAINT FKProductBOO712218 FOREIGN KEY (OperationID) REFERENCES Operation (OperationID);

ALTER TABLE IntermediateProduct ADD CONSTRAINT FKIntermedia695192 FOREIGN KEY (IntermediateID) REFERENCES ManufacturedPart (ManufacturedPartID);

ALTER TABLE StockPart ADD CONSTRAINT FKStockPart633674 FOREIGN KEY (StockPartID) REFERENCES Part (PartID);

ALTER TABLE SupplyOffer ADD CONSTRAINT FKSupplyOffe326850 FOREIGN KEY (StockPartID) REFERENCES StockPart (StockPartID);

ALTER TABLE SupplyOffer ADD CONSTRAINT FKSupplyOffe936292 FOREIGN KEY (SupplierID) REFERENCES Supplier (SupplierID);

ALTER TABLE Component ADD CONSTRAINT FKComponent725266 FOREIGN KEY (ComponentID) REFERENCES StockPart (StockPartID);

ALTER TABLE OperationInput ADD CONSTRAINT FKOperationI389517 FOREIGN KEY (OperationID) REFERENCES Operation (OperationID);

ALTER TABLE OperationInput ADD CONSTRAINT FKOperationI102091 FOREIGN KEY (PartID) REFERENCES Part (PartID);

ALTER TABLE OperationOutput ADD CONSTRAINT FKOperationO382427 FOREIGN KEY (OperationID) REFERENCES Operation (OperationID);

ALTER TABLE Operation ADD CONSTRAINT FKOperation885065 FOREIGN KEY (OperationTypeID) REFERENCES OperationType (OperationTypeID);

ALTER TABLE CanBeDoneAt ADD CONSTRAINT FKCanBeDoneA880921 FOREIGN KEY (OperationTypeID) REFERENCES OperationType (OperationTypeID);

ALTER TABLE CanBeDoneAt ADD CONSTRAINT FKCanBeDoneA399444 FOREIGN KEY (WorkstationTypeID) REFERENCES WorkstationType (WorkstationTypeID);

ALTER TABLE Workstation ADD CONSTRAINT FKWorkstatio409310 FOREIGN KEY (WorkstationTypeID) REFERENCES WorkstationType (WorkstationTypeID);

ALTER TABLE Product ADD CONSTRAINT FKProduct956046 FOREIGN KEY (ProductFamilyID) REFERENCES ProductFamily (ProductFamilyID);

ALTER TABLE "Order" ADD CONSTRAINT FKOrder286870 FOREIGN KEY (ClientID) REFERENCES Client (ClientID);

ALTER TABLE RawMaterial ADD CONSTRAINT FKRawMateria412218 FOREIGN KEY (RawMaterialID) REFERENCES StockPart (StockPartID);

ALTER TABLE RawMaterial ADD CONSTRAINT FKRawMateria681661 FOREIGN KEY (UnitID) REFERENCES RawMaterialUnit (UnitID);

ALTER TABLE ManufacturedPart ADD CONSTRAINT FKManufactur267994 FOREIGN KEY (ManufacturedPartID) REFERENCES Part (PartID);

ALTER TABLE OperationOutput ADD CONSTRAINT FKOperationO382844 FOREIGN KEY (ManufacturedPartID) REFERENCES ManufacturedPart (ManufacturedPartID);

ALTER TABLE PartReservation ADD CONSTRAINT FKPartReserv639542 FOREIGN KEY (OrderID) REFERENCES "Order" (OrderID);

ALTER TABLE PartReservation ADD CONSTRAINT FKPartReserv300566 FOREIGN KEY (StockPartID) REFERENCES StockPart (StockPartID);
