package com.linkyB.backend.user.repository;

import com.linkyB.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String userEmail);

    @Query("select u from User u where u.userEmail =:userEmail")
    User findByMail(@Param("userEmail") String userEmail);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserNickName(String nickName);

    @Query(value = "SELECT u FROM User u JOIN Match m ON m.userMatching.userId = u.userId " +
            "WHERE m.userGetMatched.userId = :userId AND m.status = 'ACTIVE' AND m.userMatchStatus = 'INACTIVE'")
    List<User> findAllByUserGetMatched(@Param("userId") long userId);


    @Query(value = "SELECT u FROM User u JOIN Match m ON m.userGetMatched.userId = u.userId " +
            "WHERE m.userMatching.userId = :userId AND m.status = 'ACTIVE' AND m.userMatchStatus = 'INACTIVE'")
    List<User> findAllByUserMatching(@Param("userId") long userId);

    @Query(value = "SELECT * FROM User u JOIN UserMatch m ON m.userMatching = u.userId " +
            "WHERE m.userGetMatched = :userId AND m.status = 'ACTIVE' AND m.userMatchStatus = 'INACTIVE' limit 4", nativeQuery = true)
    List<User> findTop4ByUserGetMatched(@Param("userId") long userId);

    @Query(value= "SELECT * FROM User u JOIN UserMatch m ON m.userGetMatched = u.userId\n" +
            "            WHERE m.userMatching = :userId AND m.status = 'ACTIVE' AND m.userMatchStatus = 'INACTIVE' limit 4", nativeQuery = true)
    List<User> findTop4ByUserMatching(@Param("userId") long userId);

    @Query(value = "SELECT u FROM User u JOIN Block b ON b.userGetBlocked.userId = u.userId " +
            "WHERE b.userGiveBlock.userId = :userId AND b.blockStatus = 'ACTIVE'")
    List<User> findAllByUserGiveBlock(@Param("userId") long userId);

    @Query(value = "SELECT * FROM User u WHERE userMajorName NOT IN (SELECT umff.blockedMajor FROM MajorForFilter umff WHERE umff.userId = ?)\n" +
            "                       AND userStudentNum IN (SELECT ugff.Grade FROM GradeForFilter ugff )\n" +
            "                       AND userMBTI NOT IN(SELECT umff2.blockedMbti FROM MbtiForFilter umff2)\n" +
            "                       AND userSex IN (SELECT uf.Gender FROM GenderForFilter uf)\n" +
            "                       AND u.gradeStatus = 'true'", nativeQuery = true)
    List<User> findTrueStudentByFilter(@Param("userId") long userId);

    @Query(value = "SELECT * FROM User u WHERE userMajorName NOT IN (SELECT umff.blockedMajor FROM MajorForFilter umff WHERE umff.userId = ?)\n" +
            "                       AND userStudentNum IN (SELECT ugff.Grade FROM GradeForFilter ugff )\n" +
            "                       AND userMBTI NOT IN(SELECT umff2.blockedMbti FROM MbtiForFilter umff2)\n" +
            "                       AND userSex IN (SELECT uf.Gender FROM GenderForFilter uf)\n" +
            "                       AND u.gradeStatus = 'false'", nativeQuery = true)
    List<User> findFalseStudentByFilter(@Param("userId") long userId);

    @Query(value = "select u from User u left join GenderForFilter g on g.user.userId = u.userId\n" +
            "left join GradeForFilter GFF ON u.userId = GFF.user.userId\n" +
            "left join MajorForFilter MFF ON u.userId = MFF.user.userId\n" +
            "left join MbtiForFilter M ON u.userId = M.user.userId\n" +
            "where u.userId= :userId")
    User findFilterByUserId(@Param("userId")long userId);

    List<User> findByuserNickNameContaining(String nickName);

    User findSchoolNameByUserId(long userId);

}