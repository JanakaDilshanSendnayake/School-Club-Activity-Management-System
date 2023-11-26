module OOD.Group.CW {
    requires  javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    //requires charm.glisten;

    opens main;
    opens SceneControlersClubAdvisors;
    opens SceneControlersStudents;
    opens CommonSceneControlers;
    opens stake_holders;
    opens Template;

}
