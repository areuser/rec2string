function %1$s (%2$s %3$s,pp_quietCut boolean default false,pp_prefix string default '') return string
is
    line type.string;
    lines varchars_t;
    idx pls_integer;
begin
 if ( not isTurnedOn ) then return null; end if;
 lines := new varchars_t();
 if %2$s.count > 0 then
    idx := %2$s.first;
    while ( idx is not null )
    loop
        putil.extend(lines,acquirePart(pp_partName => pp_prefix||'('||idx||')',pp_partValue => toString(pp_rec => pp_rec(idx),pp_quietCut => pp_quietCut,pp_prefix => pp_prefix||'('||idx||')'),pp_quietCut => pp_quietCut));
        idx := %2$s.next(idx);
    end loop;
 end if;
 return chr(10)||putil.join(pp_args => lines,pp_sep => chr(10),pp_quietCut => pp_quietCut or isQuietCut);
end;

