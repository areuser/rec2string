function %1$s (%2$s %3$s,pp_quietCut boolean default false,pp_prefix string default '') return string
is
    vPrefix type.string;
begin
    if ( not isTurnedOn ) then return null; end if;
    vPrefix := putil.ite(putil.isBlank(pp_prefix),pp_prefix,pp_prefix||'.');
    return  joinParts(pp_parts => varchars_t(
            %4$s
        ),pp_quietCut => pp_quietCut);
end;
