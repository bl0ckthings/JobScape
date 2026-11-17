USE jobscape;

DROP PROCEDURE IF EXISTS sp_statistics;

DELIMITER //

CREATE PROCEDURE sp_statistics(
    IN p_semaine SMALLINT,
    IN p_mois SMALLINT,
    IN p_annee SMALLINT,
    IN p_user_id BIGINT
)
BEGIN
    SELECT
        CASE
            WHEN p_semaine IS NOT NULL THEN DAY(a.created_at)
            WHEN p_mois IS NOT NULL THEN FLOOR((DAY(a.created_at) - 1) / 7) + 1
            ELSE MONTH(a.created_at)
        END AS timeline,
        COUNT(DISTINCT a.id) AS nombreCandidatures,
        COUNT(DISTINCT CASE
            WHEN ast.step_status IN (
                'FIRST_CONTACT', 'HR_INTERVIEW', 'MANAGER_INTERVIEW',
                'TECHNICAL_TEST', 'OFFER_RECEIVED'
            ) THEN a.id
        END) AS nombreReponses,
        COUNT(DISTINCT CASE
            WHEN ast.step_status IN (
                'HR_INTERVIEW', 'MANAGER_INTERVIEW', 'TECHNICAL_TEST'
            ) THEN a.id
        END) AS nombreEntretien,
        COUNT(DISTINCT CASE
            WHEN ast.step_status = 'OFFER_RECEIVED' OR a.status = 'OFFER_RECEIVED'
                THEN a.id
        END) AS nombreOffre
    FROM applications a
    LEFT JOIN application_steps ast ON ast.application_id = a.id
    WHERE a.user_id = p_user_id
      AND a.status NOT IN ('WISHLIST', 'REJECTED')
      AND YEAR(a.created_at) = p_annee
      AND (p_mois IS NULL OR MONTH(a.created_at) = p_mois)
      AND (
          p_semaine IS NULL
          OR FLOOR((DAY(a.created_at) - 1) / 7) + 1 = p_semaine
      )
    GROUP BY timeline
    ORDER BY timeline ASC;
END //

DELIMITER ;
