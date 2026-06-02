UPDATE companies SET plan_type = 'ESSENTIAL' WHERE plan_type = 'BASIC';
UPDATE companies SET plan_type = 'PROFESSIONAL' WHERE plan_type = 'PREMIUM';
