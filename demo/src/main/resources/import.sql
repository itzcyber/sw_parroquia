INSERT INTO `parroquiadb`.`users` (`id`, `apellido`, `capilla`, `email`, `enabled`, `nombre`, `password`) VALUES ('1', 'perez', 'Lourdes', 'juan@gmail.com', b'1', 'juan', '$2a$10$YddC4.DR3GkL1U2ZyfwIYuzZO5OLgVu7FDBiW7uswSRsAjmC7.lIO');
INSERT INTO `parroquiadb`.`users` (`id`, `apellido`, `capilla`, `email`, `enabled`, `nombre`, `password`) VALUES ('2', 'jerez', 'Sagrada Familia', 'claudio@gmail.com', b'1', 'claudio', '$2a$10$AyckiGRvyfmLf1E3CGjWY.CPV5DV5dXvQwpCBhuQlC2pQKIAnu2tW');
INSERT INTO `parroquiadb`.`users` (`id`, `apellido`, `capilla`, `email`, `enabled`, `nombre`, `password`) VALUES ('3', 'soprano', 'Chacra', 'soprano@gmail.com', b'1', 'antonio', '$2a$10$TnUq.5o.hGXj7dBNBPX5JOcaxpTgrmVMBvVCb1OYWO1dYoy9Estd2');

INSERT INTO `parroquiadb`.`roles` (`id`, `descripcion`, `nombre`) VALUES ('1', 'Gestiona el personal de la parroquia', 'Administrador');
INSERT INTO `parroquiadb`.`roles` (`id`, `descripcion`, `nombre`) VALUES ('2', 'Gestiona los certificados de la parroquia', 'Secretaria');

INSERT INTO `parroquiadb`.`users_roles` (`user_id`, `role_id`) VALUES ('1', '1');
INSERT INTO `parroquiadb`.`users_roles` (`user_id`, `role_id`) VALUES ('1', '2');
INSERT INTO `parroquiadb`.`users_roles` (`user_id`, `role_id`) VALUES ('2', '1');
INSERT INTO `parroquiadb`.`users_roles` (`user_id`, `role_id`) VALUES ('3', '2');
