SELECT k.emp_name, 
       k.area_name, 
       k.branch_name, 
       k.contact_type, 
       Sum(k.sent_count) AS SENT_COUNT, 
       Sum(k.customer_count) AS customer_count, 
       Sum(k.verified_count) AS verified_count 
FROM   (SELECT x.user_name AS emp_name, 
               e.fudesc AS area_name, 
               d.branch_name, 
               c.contact_type, 
               c.isactive, 
               Count(distinct c.customer_id) customer_count, 
               Count(*) sent_count, 
               Sum(CASE 
                     WHEN c.count_id = 0 
                          AND c.isactive = 'N' THEN 1 
                     ELSE 0 
                   END) AS verified_count 
        FROM   (SELECT b.id, 
                       b.application_country_id, 
                       b.contact_type, 
                       b.customer_id, 
                       b.isactive, 
                       b.created_date, 
                       b.created_by_id, 
                       (SELECT Count(a.id) 
                        FROM   ex_contact_verification a 
                        WHERE  a.contact_type = b.contact_type 
                               AND a.isactive = 'N' 
                               AND a.customer_id = b.customer_id 
                               AND a.id < b.id 
                               AND a.created_date > To_date('01-AUG-19', 
                                                    'DD-MON-YY')) 
                       COUNT_ID 
                FROM   ex_contact_verification b 
                WHERE  b.created_by_type = 'E' 
                       AND b.created_date > To_date('01-AUG-19', 'DD-MON-YY') 
                       AND b.created_by_id IS NOT NULL) c, 
               fs_employee x, 
               ex_country_branch d, 
               v_argp e 
        WHERE  x.employee_id = c.created_by_id 
               AND d.country_branch_id = x.country_branch_id 
               AND d.area_code = e.area_loccod                
               AND c.created_date > To_date('[[${gte}]]', 'YYYY-MM-DD HH24:MI:SS')
               AND c.created_date < To_date('[[${lte}]]', 'YYYY-MM-DD HH24:MI:SS') 
        GROUP  BY x.user_name, 
                  e.fudesc, 
                  d.branch_name, 
                  c.contact_type, 
                  c.isactive) k                   
GROUP  BY k.emp_name, 
          k.area_name, 
          k.branch_name, 
          k.contact_type 