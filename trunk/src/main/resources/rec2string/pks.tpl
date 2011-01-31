create or replace package %1$s is
/******************************************************************************
   NAME:
   PURPOSE:

   Package logic is extracted and generated based on given package level record structure
   Modifications of the code will be lost

   Points of interest :
  * QuietCut is used to cut the string after 3000 characters Default string is cut after 3000 characters
  * IgnoreBlank is used to ignore blank values Default blank values are ignored.
  * TurnedOff is used to turn the functionality of this package off; Default turned off to avoid  performance overhead of package during execution on production server

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0                    Arnold Reuser    Created
******************************************************************************/

  procedure setQuietCut(pp_mode boolean);
  function isQuietCut return boolean;
  procedure setIgnoreBlank(pp_mode boolean);
  function isIgnoreBlank return boolean;
  function isTurnedOn return boolean;
  procedure setTurnedOn(pp_mode boolean);

  function toString (pp_rec in varchars_tt,pp_quietCut boolean default false,pp_prefix string default '') return string;
  function toString (pp_rec in varchars_t,pp_quietCut boolean default false,pp_prefix string default '') return string;
  function toString (pp_rec in boolean,pp_quietCut boolean default false,pp_prefix string default '') return string;
  function toString (pp_rec in date,pp_quietCut boolean default false,pp_prefix string default '') return string;
  function toString (pp_rec in string,pp_quietCut boolean default false,pp_prefix string default '') return string;
  function toString (pp_rec in number,pp_quietCut boolean default false,pp_prefix string default '') return string;

  %2$s
  
end %1$s;
