# Overview #

An [overview](http://slidesha.re/i6o9Bj) of this project is provided as a presentation.
The [userguide](http://gcrepos.googlecode.com/files/User%20Guide%20-%20Reusable%20PLSQL%20Modules.pdf) will provide all the details.

If your a user of this project and not a developer.
Just download the latest [release](http://rec2string.googlecode.com/files/release-2.1.1.exe) and glance through the user guide to get going.

# Practical Application #

Ever faced with the problem of logging a record in PL\SQL?
Ever wondered why it's so hard to resolve this problem in a generic manner. This project is soly focused on this problem. The solution this project will provide is the ability to generate a package that can transform a record type into a string.

Just as a quick example :

```
declare
  TYPE r_prsn IS RECORD 
  (id                      ra_relations.relt_id%TYPE
  ,reference               ra_relations.relt_id%TYPE
  ,format_name             ra_relations.relt_official_full_name%TYPE
  ,ind_alive               ra_relations.relt_ind_alive__ntpr%TYPE
  ,name_clarification      ra_relations.relt_name_clarification__ntpr%TYPE
  ,first_names             ra_relations.relt_first_names__ntpr%TYPE
  ,infix                   ra_relations.relt_infix__ntpr%TYPE
  ,first_first_name        ra_relations.relt_first_first_name__ntpr%TYPE
  ,call_name               ra_relations.relt_call_name__ntpr%TYPE
  ,name_remarks            ra_relations.relt_name_remarks__ntpr%TYPE
  );

  v_new r_person_type;  
begin
   v_new.id := '1';
   v_new.call_name := 'Arnold Reuser';

   dbms_output.put_line(v_new); // !!!! COMPILE TIME ERROR !!!
   dbms_output.put_line(rec2string.toString(v_new)); // !!! NO PROBLEM AT ALL !!!
end;
```