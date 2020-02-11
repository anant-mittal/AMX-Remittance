select max(id) id, a.CREATED_BY_ID EMPLOYEE_ID, a.CONTACT_TYPE CONTACT_TYPE, a.ISACTIVE ISACTIVE, COUNT(*) SENT, count(DISTINCT a.CUSTOMER_ID) CUSTOMERS,
    SUM(
        case a.COUNT_ID 
            when 0 THEN 1
            else 0  
        end 
    ) VERIFIED
from employee_incentive a 
group by a.CREATED_BY_ID, a.CONTACT_TYPE, a.ISACTIVE 