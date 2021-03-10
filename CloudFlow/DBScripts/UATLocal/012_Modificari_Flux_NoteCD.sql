-- UPDATE LA NOTA CD -USERI ARB
UPDATE WORKFLOWTRANSITION
SET routingtype = 'PARAMETER_HIERARCHICAL_SUP',
    routingdestinationparameter ='initiator_nota'
WHERE workflow_id = (SELECT ID FROM WORKFLOW WHERE NAME = 'Nota CD - Useri ARB')
AND NAME = 'Metadate completate - verificare superior';

-- UPDATE LA NOTA CD -Nota CD - Responsabil
UPDATE WORKFLOWTRANSITION
SET routingtype = 'PARAMETER_HIERARCHICAL_SUP',
    routingdestinationparameter ='initiator_nota'
WHERE workflow_id = (SELECT ID FROM WORKFLOW WHERE NAME = 'Nota CD - Responsabil')
AND NAME = 'Metadate completate - verificare superior';