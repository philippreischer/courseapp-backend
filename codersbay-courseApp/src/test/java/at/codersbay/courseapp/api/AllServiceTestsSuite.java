package at.codersbay.courseapp.api;

import at.codersbay.courseapp.api.course.booking.BookingServiceTest;
import at.codersbay.courseapp.api.course.create.CreateCourseServiceTest;
import at.codersbay.courseapp.api.course.delete.DeleteCourseServiceTest;
import at.codersbay.courseapp.api.course.get.GetCourseServiceTest;
import at.codersbay.courseapp.api.course.update.UpdateCourseServiceTest;
import at.codersbay.courseapp.api.user.create.CreateUserServiceTest;
import at.codersbay.courseapp.api.user.delete.DeleteUserServiceTest;
import at.codersbay.courseapp.api.user.get.GetUserServiceTest;
import at.codersbay.courseapp.api.user.update.UpdateUserServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses({
        CreateUserServiceTest.class,
        GetUserServiceTest.class,
        DeleteUserServiceTest.class,
        UpdateUserServiceTest.class,

        CreateCourseServiceTest.class,
        GetCourseServiceTest.class,
        DeleteCourseServiceTest.class,
        UpdateCourseServiceTest.class,
        BookingServiceTest.class
})
@SuiteDisplayName("Alle Service-Tests")
public class AllServiceTestsSuite {
}
