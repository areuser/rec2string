create or replace package body %1$s is
    turnedOn boolean := false;
    quietCut boolean := true;
    ignoreBlank boolean := true;
    procedure setTurnedOn(pp_mode boolean)
    is
    begin
        turnedOn := pp_mode;
    end;
    function isTurnedOn return boolean
    is
    begin
        return turnedOn;
    end;
    procedure setQuietCut(pp_mode boolean)
    is
    begin
       quietCut :=  pp_mode;
    end;
    function isQuietCut return boolean
    is
    begin
        return quietCut;
    end;
    procedure setIgnoreBlank(pp_mode boolean)
    is
    begin
        ignoreBlank := pp_mode;
    end;
    function isIgnoreBlank return boolean
    is
    begin
        return ignoreBlank;
    end;

    function joinParts(pp_parts varchars_t,pp_quietCut boolean default false,pp_prefix string default '') return string
    is
        idx pls_integer;
        lines varchars_t;
        line type.string;
    begin
        lines := new varchars_t();
        idx := pp_parts.first;
        while ( idx is not null )
        loop
            line := pp_parts(idx);
            if ( isIgnoreBlank and putil.isBlank(line))
            then
                null;
            else
                putil.extend(lines,line);
            end if;
            idx := pp_parts.next(idx);
        end loop;
        return chr(10)||putil.join(pp_args => lines,pp_sep => chr(10), pp_quietCut => pp_quietCut or isQuietCut );
    end;

    function acquirePart(pp_partName string,pp_partValue string,pp_quietCut boolean default false,pp_prefix string default '') return string
    is
    begin
        if (isIgnoreBlank and  putil.isBlank(replace(pp_partValue,'"','') ) )
        then
            return null;
        else
            return putil.join(pp_args => varchars_t(pp_partName,pp_partValue),pp_sep => ':',pp_quietCut => pp_quietCut or isQuietCut);
        end if;
    end;

   function toString (pp_rec varchars_t,pp_quietCut boolean default false,pp_prefix string default '') return string
    is
    begin
        return putil.join(pp_args => pp_rec,pp_quietCut => pp_quietCut);
    end;

    function toString (pp_rec in varchars_tt,pp_quietCut boolean default false,pp_prefix string default '') return string
    is
        idx pls_integer;
        lines varchars_t;
        line type.string;
    begin
        lines := new varchars_t();
        if ( pp_rec is not null )
        then
            idx := pp_rec.first;
            while ( idx is not null )
            loop
                line := format.toString(pp_rec => pp_rec(idx),pp_quietCut => pp_quietCut);
                if ( isIgnoreBlank and putil.isBlank(line))
                then
                    null;
                else
                    putil.extend(lines,line);
                end if;
                idx := pp_rec.next(idx);
            end loop;
        end if;
        return format.toString(pp_rec => lines,pp_quietCut => pp_quietCut);
    end;

  function toString (pp_rec string,pp_quietCut boolean default false,pp_prefix string default '') return string
  is
  begin
    return '"'||pp_rec||'"';
  end;
    function toString (pp_rec boolean,pp_quietCut boolean default false,pp_prefix string default '') return string
    is
    begin
        return putil.toChar(pp_rec);
    end;

    function toString (pp_rec number,pp_quietCut boolean default false,pp_prefix string default '') return string
    is
    begin
        return '"'||pp_rec||'"';
    end;

    function toString (pp_rec date,pp_quietCut boolean default false,pp_prefix string default '') return string
    is
    begin
        return '"'||to_char(pp_rec,'yyyy-mm-dd')||'"';
   end;
  
  %2$s
  
end %1$s;

