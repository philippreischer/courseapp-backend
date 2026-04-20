package at.codersbay.courseapp;

import at.codersbay.courseapp.api.course.create.CreateCourseControllerIntegrationTest;
import at.codersbay.courseapp.api.course.delete.DeleteCourseControllerIntegrationTest;
import at.codersbay.courseapp.api.course.get.GetCourseControllerIntegrationTest;
import at.codersbay.courseapp.api.course.update.UpdateCourseControllerIntegrationTest;
import at.codersbay.courseapp.api.user.create.CreateUserControllerIntegrationTest;
import at.codersbay.courseapp.api.user.delete.DeleteUserControllerIntegrationTest;
import at.codersbay.courseapp.api.user.get.GetUserControllerIntegrationTest;
import at.codersbay.courseapp.api.user.update.UpdateUserControllerIntegrationTest;
import org.junit.platform.suite.api.*;

@Suite
@SelectClasses({

        CreateUserControllerIntegrationTest.class,
        GetUserControllerIntegrationTest.class,
        DeleteUserControllerIntegrationTest.class,
        UpdateUserControllerIntegrationTest.class,

        CreateCourseControllerIntegrationTest.class,
        GetCourseControllerIntegrationTest.class,
        DeleteCourseControllerIntegrationTest.class,
        UpdateCourseControllerIntegrationTest.class,
})
@SuiteDisplayName("Alle Integration-Tests")
class AllIntegrationTestsSuite {


}
