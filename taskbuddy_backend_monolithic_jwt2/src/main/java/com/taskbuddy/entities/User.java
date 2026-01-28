package com.taskbuddy.entities;

import java.time.LocalDate;

//JPA
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity 
@Table(name = "users") 
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = { "password" }, callSuper = true)

public class User extends BaseEntity  {

	@Column(name = "first_name", length = 30) 
	private String firstName;
	@Column(name = "last_name", length = 30)
	private String lastName;
	@NotBlank
	@Email
	@Column(unique = true, length = 50,nullable = false) 
	private String email;
	@Column(nullable = false)
	private String password;
	@Transient //skips from persistence (i.e column will not be created )
	private String confirmPassword;
	@Enumerated(EnumType.STRING) 
	@Column(name = "user_role")
	private UserRole userRole;
	@Column(unique = true, length = 14)
	private String phone;

	public User(String firstName, String lastName, String email, String password,
			String phone) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
	}
}
