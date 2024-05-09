package tn.esprit.entities;

public class User {

    private Integer id;
    private Integer cin;
    private Integer tel;
    private String nom;
    private String prenom;
    private String role;
    private String email;
    private String password;
    private String resetToken;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
        this.cin = cin;
        this.tel = tel;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.email = email;
        this.password = password;
        //this.resetToken = resetToken;
    }

    /*public User(Integer id, String nom, String email, String password, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.role = role;
    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCin() {
        return cin;
    }

    public void setCin(Integer cin) {
        this.cin = cin;
    }

    public Integer getTel() {
        return tel;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", cin=" + cin +
                ", tel=" + tel +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", resetToken='" + resetToken + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
