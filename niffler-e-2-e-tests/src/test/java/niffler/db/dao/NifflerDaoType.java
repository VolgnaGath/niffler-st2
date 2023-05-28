package niffler.db.dao;

public enum NifflerDaoType {
    JDBC {
        public NifflerUsersDAO getDao() {
            return new NifflerUsersDAOJdbc();
        }
    },
    SPRING {
        public NifflerUsersDAO getDao() {
            return new NifflerUsersDAOSpringJdbc();
        }
    },
    HIBERNATE {
        public NifflerUsersDAO getDao() {
            return new NifflerUsersDAOHibernate();
        }
    };
    public NifflerUsersDAO getDao() {
        return null;}

}
