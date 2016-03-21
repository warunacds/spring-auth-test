CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `fb_id` varchar(100) NOT NULL,
  `fb_auth_token` varchar(535) NOT NULL,
  `username` varchar(535) NOT NULL,
  `password` varchar(200) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

INSERT INTO `user` (`user_id`, `fb_id`, `fb_auth_token`, `username`, `password`) VALUES
(1, '8288282', 'jjd8388jdjjdjdhdhd', 'Mathew', ''),
(2, '39484839', 'iisjjsf9393jsjfshf', 'Waruna', ''),
(3, '48488484', 'sjsf8287bsbfhsbf78237187', 'roy', 'spring');