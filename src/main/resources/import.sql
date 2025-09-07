--TABELA USERS
--senhas 123
insert into users (cep, city, cpf, display_name, graduation_year, house_number, password, phone, professional_register, street, username) values ('1111.1111', 'Pato Branco', '111.111.111-11', 'Fulano 1', '1991', '01', '$2a$10$zVJ0LSYTBEhX7LEc1b2ND.07mCsRJo4NNfmfP6bQrUpH3DEBZuvA2', '1111', '1111', 'Rua teste1', 'fulano1@test.com');
insert into users (cep, city, cpf, display_name, graduation_year, house_number, password, phone, professional_register, street, username) values ('2222.2222', 'Beltrao', '222.222.222-22', 'Fulano 2', '2002', '22', '$2a$10$zVJ0LSYTBEhX7LEc1b2ND.07mCsRJo4NNfmfP6bQrUpH3DEBZuvA2', '2222', '2222', 'Rua teste2', 'fulano2@test.com');
insert into users (cep, city, cpf, display_name, graduation_year, house_number, password, phone, professional_register, street, username) values ('3333.3333', 'Palmas', '333.333.333-33', 'Fulano 2', '2013', '333', '$2a$10$zVJ0LSYTBEhX7LEc1b2ND.07mCsRJo4NNfmfP6bQrUpH3DEBZuvA2', '3333', '3333', 'Rua teste3', 'fulano3@test.com');
--TABELA PROPERTY
insert into property (latitude, leased, longitude, ocupation_area, total_area, user_id,name, city, state, naked_average_price, lease_average_price, farmer) values('1365', true, '1365', 'Ocupation Area1', '163.46', 1, 'Property 1', 'City 1', 'State 1', 1000.00, 500.00, 'Farmer 1');
insert into property (latitude, leased, longitude, ocupation_area, total_area, user_id, name, city, state, naked_average_price, lease_average_price, farmer) values('654', true, '654', 'Ocupation Area2', '634.13', 1, 'Property 2', 'City 2', 'State 2', 2000.00, 1000.00, 'Farmer 2');
insert into property (latitude, leased, longitude, ocupation_area, total_area, user_id,name, city, state, naked_average_price, lease_average_price, farmer) values('365', true, '365', 'Ocupation Area3', '389.16', 2, 'Property 3', 'City 3', 'State 3', 1500.00, 750.00, 'Farmer 3');
--TABELA REGION
insert into region (name) values ('Region 1');
insert into region (name) values ('Region 2');
insert into region (name) values ('Region 3');
--TABELA CITY
insert into city (city_region_id, name) values (1, 'City 1');
insert into city (city_region_id, name) values (2, 'City 2');
insert into city (city_region_id, name) values (3, 'City 3');
--TABELA PROPERTY_EQUIP_IMPROVE
insert into property_equip_improve (aquisition_date, name, percentage_cattle, property_id, quantity, type, unity_value, util_life, value_cattle) values ('2023-05-18', 'Equipament 1', '965.36', 3, 10, 'Test type', '135.13', 16, '136.16');
insert into property_equip_improve (aquisition_date, name, percentage_cattle, property_id, quantity, type, unity_value, util_life, value_cattle) values ('2023-05-16', 'Equipament 2', '965.36', 1, 22, 'Test type', '135.13', 36, '136.16');
insert into property_equip_improve (aquisition_date, name, percentage_cattle, property_id, quantity, type, unity_value, util_life, value_cattle) values ('2023-05-17', 'Equipament 3', '965.36', 1, 3, 'Test type', '135.13', 15, '136.16');
--TABELA PROPERTY_COLLABORATOR
insert into property_collaborator (collaborator_name, property_id, work_days, work_hours) values ('collaborator 1', 2, 7, 12);
insert into property_collaborator (collaborator_name, property_id, work_days, work_hours) values ('collaborator 2', 3, 7, 8);
insert into property_collaborator (collaborator_name, property_id, work_days, work_hours) values ('collaborator 3', 1, 7, 8);
--TABELA PROPERTY_AREA
insert into property_area (property_id, dairy_cattle_farming, perennial_pasture, summer_plowing, winter_plowing) values (1, 123.32, 312.32, 32.2, 32.2);
insert into property_area (property_id, dairy_cattle_farming, perennial_pasture, summer_plowing, winter_plowing) values (2, 123.32, 312.32, 32.2, 32.2);
insert into property_area (property_id, dairy_cattle_farming, perennial_pasture, summer_plowing, winter_plowing) values (3, 123.32, 312.32, 32.2, 32.2);
-- TABELA PROPERTY_TECHNICIAN
insert into property_technician (property_id, user_id) values (1, 1);
insert into property_technician (property_id, user_id) values (2, 2);
insert into property_technician (property_id, user_id) values (3, 3);
--TABELA ACTIVE_PRINCIPLE
insert into active_principle (name) values ('Active principle 1');
insert into active_principle (name) values ('Active principle 2');
insert into active_principle (name) values ('Active principle 3');
--TABELA PRODUCT_CATEGORY
insert into product_category (description) values ('Description 1');
insert into product_category (description) values ('Description 2');
insert into product_category (description) values ('Description 3');
--TABELA PRODUCT
insert into product (name, description, application_way, active_principle_id, category_id) values ('Product 1', 'Description 1', 'Way 1', 2, 3);
insert into product (name, description, application_way, active_principle_id, category_id) values ('Product 2', 'Description 2', 'Way 2', 3, 1);
insert into product (name, description, application_way, active_principle_id, category_id) values ('Product 3', 'Description 3', 'Way 3', 1, 2);
--TABELA PERENNIAL_ANNUAL_FORAGE
insert into perennial_anual_forage (area, average_cost, forage, formation_date, note, price, property_id, type, util_life) values ('165.13', '136.45', 'Forage 1', '2022-06-23', 'Note 1', '16.12', 2, 'Type 1', 13);
insert into perennial_anual_forage (area, average_cost, forage, formation_date, note, price, property_id, type, util_life) values ('365.46', '63.45', 'Forage 2', '2022-06-23', 'Note 2', '35.16', 1, 'Type 2', 16);
insert into perennial_anual_forage (area, average_cost, forage, formation_date, note, price, property_id, type, util_life) values ('65.13', '365.45', 'Forage 3', '2022-06-23', 'Note 3', '51.12', 3, 'Type 3', 15);
--TABELA FORAGE_DISPONIBILITY
insert into forage_disponibility (date, efficiency, entry, forage, kg, kg_cows, num_cows, picket_area, property_id, residue) values ('2022-06-23', '129', '15.1', 'forage 1', '16', '36', '1365', '169', 1, '16');
insert into forage_disponibility (date, efficiency, entry, forage, kg, kg_cows, num_cows, picket_area, property_id, residue) values ('2022-06-20', '465', '64.5', 'forage 2', '46', '46', '1365', '26', 2, '36');
insert into forage_disponibility (date, efficiency, entry, forage, kg, kg_cows, num_cows, picket_area, property_id, residue) values ('2022-06-15', '121', '46.9', 'forage 3', '45', '32', '1365', '265', 3, '36');
--TABELA BREED
insert into breed (breed_name) values ('Holandês');
insert into breed (breed_name) values ('Girolando');
insert into breed (breed_name) values ('Jersey');
--TABELA ANIMAL
insert into animal (animal_mother_id, born_condition, born_date, born_weight, breed_id, current_weight, ecc, gender, identifier, previous_weight, property_id, type) values (null, 'Vivo', '2019-06-23', '12', 1, '62', '32', 'F', '123', '45', 3, 'type 1');
insert into animal (animal_mother_id, born_condition, born_date, born_weight, breed_id, current_weight, ecc, gender, identifier, previous_weight, property_id, type) values (1, 'Vivo', '2023-06-23', '10', 2, '54', '36', 'F', '321', '36', 2, 'type 2');
insert into animal (animal_mother_id, born_condition, born_date, born_weight, breed_id, current_weight, ecc, gender, identifier, previous_weight, property_id, type) values (1, 'Vivo', '2022-05-16', '16', 2, '62', '36', 'M', '456', '26', 2, 'type 3');
--TABELA CULTURE
insert into culture (culture_name) values ('Culture 1');
insert into culture (culture_name) values ('Culture 2');
insert into culture (culture_name) values ('Culture 3');
--TABELA PLAGUE
insert into plague (plague_name) values ('Plague 1');
insert into plague (plague_name) values ('Plague 2');
insert into plague (plague_name) values ('Plague 3');
--TABELA DISEASE
insert into disease (disease_name) values ('Disease 1');
insert into disease (disease_name) values ('Disease 2');
insert into disease (disease_name) values ('Disease 3');
--TABELA VEGETABLE_DISEASE
insert into vegetable_disease (date, culture_id, disease_id, property_id, infestation_type) values ('2021-12-30', 3, 1, 2, 'InfestationType 1');
insert into vegetable_disease (date, culture_id, disease_id, property_id, infestation_type) values ('2022-06-15', 2, 3, 2, 'InfestationType 2');
insert into vegetable_disease (date, culture_id, disease_id, property_id, infestation_type) values ('2022-03-05', 1, 2, 3, 'InfestationType 3');
--TABELA VEGETABLE_PLAGUE
insert into vegetable_plague (date, culture_id, plague_id, property_id, infestation_type) values ('2021-03-14', 3, 1, 2, 'InfestationType 1');
insert into vegetable_plague (date, culture_id, plague_id, property_id, infestation_type) values ('2019-10-22', 2, 3, 2, 'InfestationType 2');
insert into vegetable_plague (date, culture_id, plague_id, property_id, infestation_type) values ('2021-03-14', 1, 2, 3, 'InfestationType 3');
--TABELA LAND_PRODUCT
insert into land_product (quantity, use_date, used_for, property_id) values (20, '2022-06-15', 'For 1', 2);
insert into land_product (quantity, use_date, used_for, property_id) values (36, '2023-03-26', 'For 2', 1);
insert into land_product (quantity, use_date, used_for, property_id) values (14, '2023-01-11', 'For 3', 3);
--TABELA MEDICATION
insert into medication (application_date, application_way, applied_dose, animal_id, product_id) values ('2023-02-18','Way 1','Dose 1', 3, 2);
insert into medication (application_date, application_way, applied_dose, animal_id, product_id) values ('2022-12-29','Way 2','Dose 2', 1, 3);
insert into medication (application_date, application_way, applied_dose, animal_id, product_id) values ('2023-05-03','Way 3','Dose 3', 2, 1);
--TABLE ANIMAL_DISEASES
insert into animal_diseases (diagnosis, diagnosis_date, animal_id) values ('Diagnosis 1', '2022-08-20', 2);
insert into animal_diseases (diagnosis, diagnosis_date, animal_id) values ('Diagnosis 2', '2022-11-29', 3);
insert into animal_diseases (diagnosis, diagnosis_date, animal_id) values ('Diagnosis 3', '2022-01-18', 2);
--TABLE ANIMAL_PURCHASES
insert into animal_purchases (birth_date, date_purchase, value, animal_id) values ('2019-06-23', '2019-10-28', '2156.13', 1);
--TABLE ANIMAL_SALES
insert into animal_sales (date_sale, destination, reason, value, animal_id) values ('2023-08-16', 1, 0, '3546.00', 2);
insert into animal_sales (date_sale, destination, reason, value, animal_id) values ('2023-08-24', 1, 2, '3123.00', 3);
--TABELA PREGNANCY_DIAGNOSE
insert into pregnancy_diagnose (diagnose_date, lastia, animal_id) values ('2023-05-20', '2023-01-16', 1);
insert into pregnancy_diagnose (diagnose_date, lastia, animal_id) values ('2023-06-03', '2023-02-06', 2);
--TABELA INSEMINATION
insert into insemination (bull, ia_date, animal_id) values ('bull 1', '2023-01-16', 1);
insert into insemination (bull, ia_date, animal_id) values ('bull 2', '2023-02-06', 2);
--TABELA MASTITIS
insert into mastitis (ad, ae, cmt_result, diagnose_date, mastitis_type, pd, pe, animal_id) values ('ad 1','ae 1','cmt_result 1','2023-02-18','Type 1','pd 1','pe 1',3);


