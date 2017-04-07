# lightinject

Simple library for Android dependency injection. It allows to initialize objects automatically based on certain configuration.

#### 1. Add the following in the `build.gradle` of your app:

```gradle
buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath 'com.uphyca.gradle:gradle-android-aspectj-plugin:0.9.+'
    }
}

apply plugin: 'android-aspectj'
```

#### 2. Create your Binding Configuration in the `onCreate()` method of your `Application` class:

```java
new BindingConfiguration() {
	@Override
	public void configure() {
		from(TextGenerator.class).to(TextLowercaseGenerator.class).add();
	}
}.configure();
```

There are some options to use in the configuration method:
- `from` -> Specifies the type that is going to be injected.
- `to` -> When an field with type `from` is injected, it will be injected to a instance of the class specified in `to`.
- `provider` -> Allows to create a class (that should implement `Provider`) that will have the instantiation logic.
- `singleton` -> The instance will be the same for all the injections. Otherwise, a new instance will be created per injection.

#### 3. Inject your fields!

```java
public class MainActivity extends AppCompatActivity {

	@Inject private TextGenerator textGenerator; // It will be instantiated with the value specified in MyApplication

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView textView = (TextView) findViewById(R.id.text_view);
		textView.setText(textGenerator.generate());
	}
}
```

#### 4. Example
You have available an example in the [following link](https://github.com/jorgearaujo/lightinject-example).
