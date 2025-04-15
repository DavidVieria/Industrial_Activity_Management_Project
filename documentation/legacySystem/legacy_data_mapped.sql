-- This file contains SQL statements to insert data from a legacy system into the current database.

INSERT INTO ClientType (ClientTypeID, ClientTypeName) VALUES ('I', 'Individual');
INSERT INTO ClientType (ClientTypeID, ClientTypeName) VALUES ('C', 'Company');


INSERT INTO ClientState (ClientStateID, ClientStateName) VALUES ('A', 'Active');
INSERT INTO ClientState (ClientStateID, ClientStateName) VALUES ('I', 'Inactive');


INSERT INTO Address (AddressID, Street, ZipCode, Town, Country) VALUES (1, 'Tv. Augusto Lessa 23', '4200-047', 'Porto', 'Portugal');
INSERT INTO Address (AddressID, Street, ZipCode, Town, Country) VALUES (2, 'R. Dr. Barros 93', '4465-219', 'São Mamede de Infesta', 'Portugal');
INSERT INTO Address (AddressID, Street, ZipCode, Town, Country) VALUES (3, 'EDIFICIO CRISTAL lj18, R. António Correia de Carvalho 88', '4400-023', 'Vila Nova de Gaia', 'Portugal');
INSERT INTO Address (AddressID, Street, ZipCode, Town, Country) VALUES (4, 'Křemencova 11', '110 00', 'Nové Město', 'Czechia');


INSERT INTO Client (ClientID, ClientTypeID, AddressID, Name, VATIN, PhoneNumber, EmailAddress) VALUES (456, 'C', 1, 'Carvalho & Carvalho, Lda', 'PT501245987', 003518340500, 'idont@care.com');
INSERT INTO Client (ClientID, ClientTypeID, AddressID, Name, VATIN, PhoneNumber, EmailAddress) VALUES (785, 'C', 2, 'Tudo para a casa, Lda', 'PT501245488', 003518340500, 'me@neither.com');
INSERT INTO Client (ClientID, ClientTypeID, AddressID, Name, VATIN, PhoneNumber, EmailAddress) VALUES (657, 'C', 3, 'Sair de Cena', 'PT501242417', 003518340500, 'some@email.com');
INSERT INTO Client (ClientID, ClientTypeID, AddressID, Name, VATIN, PhoneNumber, EmailAddress) VALUES (348, 'C', 4, 'U Fleku', 'CZ6451237810', 004201234567, 'some.random@email.cz');


INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (1, 785, 2, TO_DATE('23/09/2024', 'DD/MM/YYYY'), TO_DATE('23/09/2024', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (2, 657, 3, TO_DATE('26/09/2024', 'DD/MM/YYYY'), TO_DATE('26/09/2024', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (3, 348, 4, TO_DATE('25/09/2024', 'DD/MM/YYYY'), TO_DATE('25/09/2024', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (4, 785, 2, TO_DATE('25/09/2024', 'DD/MM/YYYY'), TO_DATE('25/09/2024', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (5, 657, 3, TO_DATE('25/09/2024', 'DD/MM/YYYY'), TO_DATE('25/09/2024', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (6, 348, 4, TO_DATE('26/09/2024', 'DD/MM/YYYY'), TO_DATE('26/09/2024', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (7, 456, 1, TO_DATE('26/09/2024', 'DD/MM/YYYY'), TO_DATE('26/09/2024', 'DD/MM/YYYY'));


INSERT INTO RawMaterialUnit (UnitID, UnitName, Symbol) VALUES (1, 'Milliliter', 'ml');


INSERT INTO Part (PartID, Description) VALUES ('AS12945T22', '5l 22 cm aluminium and teflon non stick pot');
INSERT INTO Part (PartID, Description) VALUES ('AS12945S22', '5l 22 cm stainless steel pot');
INSERT INTO Part (PartID, Description) VALUES ('AS12946S22', '5l 22 cm stainless steel pot bottom');
INSERT INTO Part (PartID, Description) VALUES ('AS12947S22', '22 cm stainless steel lid');
INSERT INTO Part (PartID, Description) VALUES ('AS12945S20', '3l 20 cm stainless steel pot');
INSERT INTO Part (PartID, Description) VALUES ('AS12946S20', '3l 20 cm stainless steel pot bottom');
INSERT INTO Part (PartID, Description) VALUES ('AS12947S20', '20 cm stainless steel lid');
INSERT INTO Part (PartID, Description) VALUES ('AS12945S17', '2l 17 cm stainless steel pot');
INSERT INTO Part (PartID, Description) VALUES ('AS12945P17', '2l 17 cm stainless steel souce pan');
INSERT INTO Part (PartID, Description) VALUES ('AS12945S48', '17 cm stainless steel lid');
INSERT INTO Part (PartID, Description) VALUES ('AS12945G48', '17 cm glass lid');
INSERT INTO Part (PartID, Description) VALUES ('IP12945A01', '250 mm 5 mm stailess steel disc');
INSERT INTO Part (PartID, Description) VALUES ('IP12945A02', '220 mm pot base phase 1');
INSERT INTO Part (PartID, Description) VALUES ('IP12945A03', '220 mm pot base phase 2');
INSERT INTO Part (PartID, Description) VALUES ('IP12945A04', '220 mm pot base final');
INSERT INTO Part (PartID, Description) VALUES ('IP12947A01', '250 mm 1 mm stailess steel disc');
INSERT INTO Part (PartID, Description) VALUES ('IP12947A02', '220 mm lid pressed');
INSERT INTO Part (PartID, Description) VALUES ('IP12947A03', '220 mm lid polished');
INSERT INTO Part (PartID, Description) VALUES ('IP12947A04', '220 mm lid with handle');
INSERT INTO Part (PartID, Description) VALUES ('IP12945A32', '200 mm pot base phase 1');
INSERT INTO Part (PartID, Description) VALUES ('IP12945A33', '200 mm pot base phase 2');
INSERT INTO Part (PartID, Description) VALUES ('IP12945A34', '200 mm pot base final');
INSERT INTO Part (PartID, Description) VALUES ('IP12947A32', '200 mm lid pressed');
INSERT INTO Part (PartID, Description) VALUES ('IP12947A33', '200 mm lid polished');
INSERT INTO Part (PartID, Description) VALUES ('IP12947A34', '200 mm lid with handle');
INSERT INTO Part (PartID, Description) VALUES ('PN12344A21', 'Screw M6 35 mm');
INSERT INTO Part (PartID, Description) VALUES ('PN52384R50', '300x300 mm 5 mm stainless steel sheet');
INSERT INTO Part (PartID, Description) VALUES ('PN52384R10', '300x300 mm 1 mm stainless steel sheet');
INSERT INTO Part (PartID, Description) VALUES ('PN18544A21', 'Rivet 6 mm');
INSERT INTO Part (PartID, Description) VALUES ('PN18544C21', 'Stainless steel handle model U6');
INSERT INTO Part (PartID, Description) VALUES ('PN18324C54', 'Stainless steel handle model R12');
INSERT INTO Part (PartID, Description) VALUES ('PN52384R45', '250x250 mm 5mm stainless steel sheet');
INSERT INTO Part (PartID, Description) VALUES ('PN52384R12', '250x250 mm 1mm stainless steel sheet');
INSERT INTO Part (PartID, Description) VALUES ('PN18324C91', 'Stainless steel handle model S26');
INSERT INTO Part (PartID, Description) VALUES ('PN18324C51', 'Stainless steel handle model R11');
INSERT INTO Part (PartID, Description) VALUES ('PN94561L67', 'Coolube 2210XP');


INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('AS12945T22');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('AS12945S22');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('AS12946S22');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('AS12947S22');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('AS12945S20');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('AS12946S20');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('AS12947S20');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('AS12945S17');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('AS12945P17');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('AS12945S48');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('AS12945G48');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12945A01');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12945A02');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12945A03');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12945A04');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12947A01');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12947A02');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12947A03');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12947A04');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12945A32');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12945A33');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12945A34');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12947A32');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12947A33');
INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('IP12947A34');


INSERT INTO ProductFamily (ProductFamilyID, ProductFamilyName) VALUES (125, 'Pro Line pots');
INSERT INTO ProductFamily (ProductFamilyID, ProductFamilyName) VALUES (130, 'La Belle pots');
INSERT INTO ProductFamily (ProductFamilyID, ProductFamilyName) VALUES (132, 'Pro Line pans');
INSERT INTO ProductFamily (ProductFamilyID, ProductFamilyName) VALUES (145, 'Pro Line lids');
INSERT INTO ProductFamily (ProductFamilyID, ProductFamilyName) VALUES (146, 'Pro Clear lids');


INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('AS12945T22', 130, 'La Belle 22 5l pot');
INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('AS12945S22', 125, 'Pro 22 5l pot');
INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('AS12945S20', 125, 'Pro 20 3l pot');
INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('AS12945S17', 125, 'Pro 17 2l pot');
INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('AS12945P17', 132, 'Pro 17 2l sauce pan');
INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('AS12945S48', 145, 'Pro 17 lid');
INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('AS12945G48', 146, 'Pro Clear 17 lid');
INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('AS12946S22', 125, 'Pro 22 5l pot bottom');
INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('AS12947S22', 145, 'Pro 22 lid');
INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('AS12946S20', 125, 'Pro 20 3l pot bottom');
INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('AS12947S20', 145, 'Pro 20 lid');


INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12945A01');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12945A02');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12945A03');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12945A04');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12947A01');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12947A02');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12947A03');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12947A04');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12945A32');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12945A33');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12945A34');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12947A32');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12947A33');
INSERT INTO IntermediateProduct (IntermediateID) VALUES ('IP12947A34');


INSERT INTO StockPart (StockPartID, CurrentStock, MinimumStock) VALUES ('PN12344A21', 1000, 500);
INSERT INTO StockPart (StockPartID, CurrentStock, MinimumStock) VALUES ('PN52384R50', 850, 200);
INSERT INTO StockPart (StockPartID, CurrentStock, MinimumStock) VALUES ('PN52384R10', 658, 200);
INSERT INTO StockPart (StockPartID, CurrentStock, MinimumStock) VALUES ('PN18544A21', 757, 300);
INSERT INTO StockPart (StockPartID, CurrentStock, MinimumStock) VALUES ('PN18544C21', 603, 150);
INSERT INTO StockPart (StockPartID, CurrentStock, MinimumStock) VALUES ('PN18324C54', 614, 150);
INSERT INTO StockPart (StockPartID, CurrentStock, MinimumStock) VALUES ('PN18324C51', 617, 150);
INSERT INTO StockPart (StockPartID, CurrentStock, MinimumStock) VALUES ('PN52384R45', 655, 150);
INSERT INTO StockPart (StockPartID, CurrentStock, MinimumStock) VALUES ('PN52384R12', 550, 100);
INSERT INTO StockPart (StockPartID, CurrentStock, MinimumStock) VALUES ('PN18324C91', 650, 100);
INSERT INTO StockPart (StockPartID, CurrentStock, MinimumStock) VALUES ('PN94561L67', 800, 300);


INSERT INTO Component (ComponentID) VALUES ('PN12344A21');
INSERT INTO Component (ComponentID) VALUES ('PN52384R50');
INSERT INTO Component (ComponentID) VALUES ('PN52384R10');
INSERT INTO Component (ComponentID) VALUES ('PN18544A21');
INSERT INTO Component (ComponentID) VALUES ('PN18544C21');
INSERT INTO Component (ComponentID) VALUES ('PN18324C54');
INSERT INTO Component (ComponentID) VALUES ('PN18324C51');
INSERT INTO Component (ComponentID) VALUES ('PN52384R45');
INSERT INTO Component (ComponentID) VALUES ('PN52384R12');
INSERT INTO Component (ComponentID) VALUES ('PN18324C91');


INSERT INTO RawMaterial (RawMaterialID, UnitID) VALUES ('PN94561L67', 1);


INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (1, 'AS12945S22', 5);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (1, 'AS12945S20', 15);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (2, 'AS12945S22', 10);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (2, 'AS12945P17', 20);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (3, 'AS12945S22', 10);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (3, 'AS12945S20', 10);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (4, 'AS12945S20', 24);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (4, 'AS12945S22', 16);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (4, 'AS12945S17', 8);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (5, 'AS12945S22', 12);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (6, 'AS12945S17', 8);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (6, 'AS12945P17', 16);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (7, 'AS12945S22', 8);


INSERT INTO BOO (ProductID) VALUES ('AS12946S22');
INSERT INTO BOO (ProductID) VALUES ('AS12947S22');
INSERT INTO BOO (ProductID) VALUES ('AS12945S22');
INSERT INTO BOO (ProductID) VALUES ('AS12946S20');
INSERT INTO BOO (ProductID) VALUES ('AS12947S20');
INSERT INTO BOO (ProductID) VALUES ('AS12945S20');


INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5647, 'Disc cutting');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5649, 'Initial pot base pressing');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5651, 'Final pot base pressing');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5653, 'Pot base finishing');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5655, 'Lid pressing');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5657, 'Lid finishing');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5659, 'Pot handles riveting');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5661, 'Lid handle screw');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5663, 'Pot test and packaging');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5665, 'Handle welding');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5667, 'Lid polishing');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5669, 'Pot base polishing');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5671, 'Teflon painting');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5681, 'Initial pan base pressing');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5682, 'Final pan base pressing');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5683, 'Pan base finishing');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5685, 'Handle gluing');
INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (5688, 'Pan test and packaging');


INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('A4578', '600t cold forging stamping press');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('A4588', '600t cold forging precision stamping press');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('A4598', '1000t cold forging precision stamping press');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('S3271', 'Handle rivet');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('K3675', 'Packaging');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('K3676', 'Packaging for large itens');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('C5637', 'Border trimming');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('D9123', 'Spot welding');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('Q5478', 'Teflon application station');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('Q3547', 'Stainless steel polishing');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('T3452', 'Assembly T1');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('G9273', 'Circular glass cutting');
INSERT INTO WorkstationType (WorkstationTypeID, WorkstationTypeName) VALUES ('G9274', 'Glass trimming');


INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5647, 'A4578', 250, 30);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5647, 'A4588', 250, 45);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5647, 'A4598', 350, 45);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5649, 'A4588', 230, 30);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5649, 'A4598', 235, 30);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5651, 'A4588', 320, 45);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5651, 'A4598', 330, 45);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5653, 'C5637', 400, 45);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5655, 'A4588', 150, 30);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5655, 'A4598', 155, 30);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5657, 'C5637', 350, 45);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5659, 'S3271', 700, 60);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5661, 'T3452', 295, 30);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5663, 'K3675', 385, 45);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5665, 'D9123', 400, 45);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5667, 'Q3547', 1400, 80);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5669, 'Q3547', 475, 45);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5671, 'Q5478', 700, 60);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5681, 'A4588', 300, 30);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5681, 'A4598', 310, 30);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5682, 'A4588', 385, 30);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5682, 'A4598', 395, 45);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5683, 'C5637', 500, 45);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5685, 'D9123', 350, 30);
INSERT INTO CanBeDoneAt (OperationTypeID, WorkstationTypeID, MaximumExecutionTime, SetupTime) VALUES (5688, 'K3675', 550, 45);


INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (9875, 'A4578', 'Press 01', '220-630t cold forging press');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (9886, 'A4578', 'Press 02', '220-630t cold forging press');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (9847, 'A4588', 'Press 03', '220-630t precision cold forging press');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (9855, 'A4588', 'Press 04', '160-1000t precison cold forging press');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (8541, 'S3271', 'Rivet 02', 'Rivet station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (8543, 'S3271', 'Rivet 03', 'Rivet station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (6814, 'K3675', 'Packaging 01', 'Packaging station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (6815, 'K3675', 'Packaging 02', 'Packaging station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (6816, 'K3675', 'Packaging 03', 'Packaging station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (6821, 'K3675', 'Packaging 04', 'Packaging station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (6822, 'K3676', 'Packaging 05', 'Packaging station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (8167, 'D9123', 'Welding 01', 'Spot welding staion');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (8170, 'D9123', 'Welding 02', 'Spot welding staion');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (8171, 'D9123', 'Welding 03', 'Spot welding staion');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (7235, 'T3452', 'Assembly 01', 'Product assembly station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (7236, 'T3452', 'Assembly 02', 'Product assembly station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (7238, 'T3452', 'Assembly 03', 'Product assembly station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (5124, 'C5637', 'Trimming 01', 'Metal trimming station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (4123, 'Q3547', 'Polishing 01', 'Metal polishing station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (4124, 'Q3547', 'Polishing 02', 'Metal polishing station');
INSERT INTO Workstation (WorkstationID, WorkstationTypeID, WorkstationName, Description) VALUES (4125, 'Q3547', 'Polishing 03', 'Metal polishing station');


INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (100, 5647, 120);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (103, 5649, 90);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (112, 5651, 120);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (114, 5653, 300);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (115, 5659, 600);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (120, 5647, 105);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (121, 5655, 60);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (122, 5657, 240);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (123, 5661, 150);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (124, 5667, 1200);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (130, 5663, 240);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (150, 5647, 120);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (151, 5649, 90);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (152, 5651, 120);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (153, 5653, 320);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (154, 5659, 600);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (160, 5647, 90);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (161, 5655, 60);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (162, 5657, 240);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (163, 5661, 150);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (164, 5667, 1200);
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (170, 5663, 240);


INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12946S22', 100, 1, '103');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12946S22', 103, 2, '112');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12946S22', 112, 3, '114');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12946S22', 114, 4, '115');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12946S22', 115, 5, 'None');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12947S22', 120, 1, '121');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12947S22', 121, 2, '122');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12947S22', 122, 3, '123');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12947S22', 123, 4, '124');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12947S22', 124, 5, 'None');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12945S22', 130, 1, 'None');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12946S20', 150, 1, '151');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12946S20', 151, 2, '152');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12946S20', 152, 3, '153');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12946S20', 153, 4, '154');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12946S20', 154, 5, 'None');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12947S20', 160, 1, '161');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12947S20', 161, 2, '162');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12947S20', 162, 3, '163');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12947S20', 163, 4, '164');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12947S20', 164, 5, 'None');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12945S20', 170, 1, 'None');


INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (100, 'PN52384R50', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (103, 'IP12945A01', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (103, 'PN94561L67', 5);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (112, 'IP12945A02', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (112, 'PN94561L67', 5);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (114, 'IP12945A03', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (115, 'IP12945A04', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (115, 'PN18544A21', 4);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (115, 'PN18544C21', 2);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (120, 'PN52384R10', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (121, 'IP12947A01', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (121, 'PN94561L67', 5);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (122, 'IP12947A02', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (123, 'IP12947A03', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (123, 'PN18324C54', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (123, 'PN12344A21', 3);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (124, 'IP12947A04', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (130, 'AS12947S22', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (130, 'AS12946S22', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (150, 'PN52384R50', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (151, 'IP12945A01', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (151, 'PN94561L67', 5);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (152, 'IP12945A32', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (152, 'PN94561L67', 5);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (153, 'IP12945A33', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (154, 'IP12945A34', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (154, 'PN18544C21', 2);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (154, 'PN18544A21', 4);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (160, 'PN52384R10', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (161, 'IP12947A01', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (161, 'PN94561L67', 5);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (162, 'IP12947A32', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (163, 'IP12947A33', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (163, 'PN18324C51', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (163, 'PN12344A21', 3);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (164, 'IP12947A34', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (170, 'AS12946S20', 1);
INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (170, 'AS12947S20', 1);


INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (100, 'IP12945A01', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (103, 'IP12945A02', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (112, 'IP12945A03', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (114, 'IP12945A04', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (115, 'AS12946S22', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (120, 'IP12947A01', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (121, 'IP12947A02', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (122, 'IP12947A03', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (123, 'IP12947A04', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (124, 'AS12947S22', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (130, 'AS12945S22', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (150, 'IP12945A01', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (151, 'IP12945A32', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (152, 'IP12945A33', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (153, 'IP12945A34', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (154, 'AS12946S20', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (160, 'IP12947A01', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (161, 'IP12947A32', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (162, 'IP12947A33', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (163, 'IP12947A34', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (164, 'AS12947S20', 1);
INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (170, 'AS12945S20', 1);


INSERT INTO Supplier (SupplierID, Name, PhoneNumber, EmailAddress) VALUES (12298, 'SteelWorks Ltd.', 351234567893, 'contact@steelworks.com');
INSERT INTO Supplier (SupplierID, Name, PhoneNumber, EmailAddress) VALUES (12345, 'Fasteners Inc.', 351987654336, 'sales@fastenersinc.com');


INSERT INTO SupplyOffer (SupplierID, StockPartID, StartDate, EndDate, MinimumQuantity, UnitCost) VALUES (12345, 'PN18544C21', TO_DATE('01/10/2023', 'DD/MM/YYYY'), NULL, 20, 1.25);
INSERT INTO SupplyOffer (SupplierID, StockPartID, StartDate, EndDate, MinimumQuantity, UnitCost) VALUES (12345, 'PN18324C54', TO_DATE('01/10/2023', 'DD/MM/YYYY'), TO_DATE('29/02/2024', 'DD/MM/YYYY'), 10, 1.70);
INSERT INTO SupplyOffer (SupplierID, StockPartID, StartDate, EndDate, MinimumQuantity, UnitCost) VALUES (12345, 'PN18324C54', TO_DATE('01/04/2024', 'DD/MM/YYYY'), NULL, 16, 1.80);
INSERT INTO SupplyOffer (SupplierID, StockPartID, StartDate, EndDate, MinimumQuantity, UnitCost) VALUES (12345, 'PN18324C51', TO_DATE('01/07/2023', 'DD/MM/YYYY'), TO_DATE('31/03/2024', 'DD/MM/YYYY'), 30, 1.90);
INSERT INTO SupplyOffer (SupplierID, StockPartID, StartDate, EndDate, MinimumQuantity, UnitCost) VALUES (12345, 'PN18324C51', TO_DATE('01/04/2024', 'DD/MM/YYYY'), NULL, 20, 1.90);
INSERT INTO SupplyOffer (SupplierID, StockPartID, StartDate, EndDate, MinimumQuantity, UnitCost) VALUES (12298, 'PN18544C21', TO_DATE('01/09/2023', 'DD/MM/YYYY'), NULL, 10, 1.35);
INSERT INTO SupplyOffer (SupplierID, StockPartID, StartDate, EndDate, MinimumQuantity, UnitCost) VALUES (12298, 'PN18324C54', TO_DATE('01/08/2023', 'DD/MM/YYYY'), TO_DATE('29/01/2024', 'DD/MM/YYYY'), 10, 1.80);
INSERT INTO SupplyOffer (SupplierID, StockPartID, StartDate, EndDate, MinimumQuantity, UnitCost) VALUES (12298, 'PN18324C54', TO_DATE('15/02/2024', 'DD/MM/YYYY'), NULL, 20, 1.75);
INSERT INTO SupplyOffer (SupplierID, StockPartID, StartDate, EndDate, MinimumQuantity, UnitCost) VALUES (12298, 'PN18324C51', TO_DATE('01/08/2023', 'DD/MM/YYYY'), TO_DATE('31/05/2024', 'DD/MM/YYYY'), 40, 1.80);
INSERT INTO SupplyOffer (SupplierID, StockPartID, StartDate, EndDate, MinimumQuantity, UnitCost) VALUES (12298, 'PN12344A21', TO_DATE('01/07/2023', 'DD/MM/YYYY'), NULL, 200, 0.65);